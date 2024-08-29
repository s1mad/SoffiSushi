package com.example.soffisushi.presentation.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soffisushi.data.remote.firebase.model.Category
import com.example.soffisushi.data.remote.firebase.model.Delivery
import com.example.soffisushi.data.remote.firebase.model.Point
import com.example.soffisushi.data.remote.firebase.model.Product
import com.example.soffisushi.data.remote.firebase.model.Stock
import com.example.soffisushi.data.remote.retrofit.api.FrontpadApi
import com.example.soffisushi.data.remote.retrofit.model.ApiResponseNewOrder
import com.example.soffisushi.domain.model.AddressAndDocumentId
import com.example.soffisushi.domain.model.SingleProduct
import com.example.soffisushi.domain.model.toSingleProduct
import com.example.soffisushi.util.AdditionalInfo
import com.example.soffisushi.util.Address
import com.example.soffisushi.util.DeliveryInfo
import com.example.soffisushi.util.MoneyInfo
import com.example.soffisushi.util.Status
import com.example.soffisushi.util.User
import com.example.soffisushi.util.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.TimeZone
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

private const val IS_FIRST_ENTRANCE_SHARED_PREF = "IS_FIRST_ENTRANCE_SHARED_PREF"
private const val POINT_DOCUMENT_ID_SHARED_PREF = "POINT_DOCUMENT_ID_SHARED_PREF"
private const val CART_PRODUCTS_SHARED_PREF = "CART_PRODUCTS_SHARED_PREF"
private const val DELIVERY_ADDRESS_SHARED_PREF = "DELIVERY_ADDRESS_SHARED_PREF"
private const val USER_INFO_SHARED_PREF = "USER_INFO_SHARED_PREF"

class SoffiSushiViewModel(
    private val sharedPreferences: SharedPreferences,
    private val firestore: FirebaseFirestore,
    private val frontpadApi: FrontpadApi
) : ViewModel() {
    private val gson = Gson()

    // Point

    private var _pointDocumentId: Status<String> = Status.Pending
    val pointDocumentId: Status<String> get() = _pointDocumentId

    private fun savePointDocumentId() {
        if (_pointDocumentId is Status.Success) {
            with(sharedPreferences.edit()) {
                putString(
                    POINT_DOCUMENT_ID_SHARED_PREF,
                    (_pointDocumentId as Status.Success).data
                )
                apply()
            }
        }
    }

    fun loadPointDocumentId(onComplete: () -> Unit = {}) {
        val getNewPointDocumentId = fun() {
            firestore.collection("points")
                .limit(1)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    _pointDocumentId = Status.Success(querySnapshot.documents[0].id)
                    onComplete()
                }.addOnFailureListener { exception ->
                    _pointDocumentId = Status.Error(exception)
                }
        }

        val savedId = sharedPreferences.getString(POINT_DOCUMENT_ID_SHARED_PREF, null)
        if (savedId != null) {
            firestore.collection("points")
                .document(savedId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        _pointDocumentId = Status.Success(savedId)
                        onComplete()
                    } else {
                        getNewPointDocumentId()
                    }
                }.addOnFailureListener { exception ->
                    _pointDocumentId = Status.Error(exception)
                }
        } else {
            getNewPointDocumentId()
        }
    }

    private val _selectedPoint = mutableStateOf<Status<Point>>(Status.Pending)
    val selectedPoint get() = _selectedPoint

    private var _selectedPointRegistration: ListenerRegistration? = null
    fun setPointListener(onComplete: () -> Unit = {}) {
        _selectedPointRegistration?.remove()
        _selectedPoint.value = Status.Loading
        _selectedPointRegistration = firestore.collection("points")
            .document((_pointDocumentId as Status.Success).data)
            .addSnapshotListener { documentSnapshot, exception ->
                if (exception != null) {
                    _selectedPoint.value = Status.Error(exception)
                    return@addSnapshotListener
                }

                val point = documentSnapshot!!.toObject(Point::class.java)
                if (point != null) {
                    _selectedPoint.value = Status.Success(point)
                    onComplete()
                } else {
                    _selectedPoint.value =
                        Status.Error(Exception("setPointListener(): point == null"))
                }
            }
    }


    fun editSelectedPoint(documentId: String) {
        clearCart()

        _pointDocumentId = Status.Success(documentId)
        savePointDocumentId()

        setPointListener(onComplete = {
            setProductsListener()
            pointOpenChecker()
        })

        _categories.value = Status.Pending
        _stocks.value = Status.Pending
        _deliveryInfo.value = _deliveryInfo.value.copy(cities = Status.Pending)
    }

    private val _pointIsOpen = mutableStateOf(false)
    val pointIsOpen get() = _pointIsOpen

    private var pointCheckerJob: Job? = null
    fun pointOpenChecker() {
        pointCheckerJob?.cancel()
        pointCheckerJob = viewModelScope.launch {
            while (true) {
                when (val result = _selectedPoint.value) {
                    is Status.Success -> {
                        val point = result.data
                        val currentTime = Calendar
                            .getInstance(TimeZone.getTimeZone(point.timeZone.trim()))
                            .run {
                                val hour = get(Calendar.HOUR_OF_DAY)
                                val minute = get(Calendar.MINUTE)
                                val second = get(Calendar.SECOND)
                                val millis = get(Calendar.MILLISECOND)
                                (hour * 3600000) + (minute * 60000) + (second * 1000) + millis
                            }
                        _pointIsOpen.value =
                            point.open && currentTime in point.startTime..point.endTime
                    }

                    else -> {
                        _pointIsOpen.value = false
                    }
                }
                delay(1000 * 30)
            }
        }
    }

    // Points

    private val _points = mutableStateOf<Status<List<AddressAndDocumentId>>>(Status.Pending)
    val points get() = _points

    fun getPoints() {
        _points.value = Status.Loading
        firestore.collection("points").get()
            .addOnSuccessListener { querySnapshot ->
                _points.value = Status.Success(
                    querySnapshot.documents.map { document ->
                        AddressAndDocumentId(
                            address = document.data!!["address"].toString(),
                            documentId = document.id
                        )
                    })
            }.addOnFailureListener { exception ->
                _points.value =
                    Status.Error(Exception("Не удалось загрузить данные: points"))
            }
    }

    // Stocks

    private val _stocks = mutableStateOf<Status<List<Stock>>>(Status.Pending)
    val stocks get() = _stocks

    fun getStocks() {
        _stocks.value = Status.Loading
        firestore.collection("points")
            .document((_pointDocumentId as Status.Success).data)
            .collection("stocks")
            .get()
            .addOnSuccessListener { querySnapshot ->
                _stocks.value = Status.Success(querySnapshot.toObjects(Stock::class.java))
            }.addOnFailureListener { exception ->
                _stocks.value = Status.Error(Exception("Не удалось загрузить список акций"))
            }
    }

    // Categories

    private val _categories = mutableStateOf<Status<List<Category>>>(Status.Pending)
    val categories get() = _categories

    val selectedCategory = mutableStateOf<Category?>(null) // Для TopBar

    fun getCategories() {
        _categories.value = Status.Loading
        firestore.collection("points")
            .document((_pointDocumentId as Status.Success).data)
            .collection("categories")
            .get()
            .addOnSuccessListener { querySnapshot ->
                _categories.value = Status.Success(querySnapshot.toObjects(Category::class.java))
            }.addOnFailureListener { exception ->
                _categories.value = Status.Error(Exception("Не удалось загрузить список категорий"))
            }
    }

    // Products

    private val _products = mutableStateOf<Status<List<Product>>>(Status.Pending)
    val products get() = _products

    val productToDetails = mutableStateOf<Product?>(null) // DetailsCard

    private var _productsRegistration: ListenerRegistration? = null
    fun setProductsListener() {
        _productsRegistration?.remove()
        _products.value = Status.Loading
        _productsRegistration = firestore.collection("points")
            .document((_pointDocumentId as Status.Success).data)
            .collection("products")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    _products.value = Status.Error(exception)
                    return@addSnapshotListener
                }
                _products.value = Status.Success(snapshot!!.toObjects(Product::class.java))
                loadCart()
            }
    }

    // Cart

    private val _cartProducts = mutableStateMapOf<SingleProduct, Int>()
    val cartProducts: Map<SingleProduct, Int> get() = _cartProducts

    val sumOfCartProducts
        get() = _cartProducts.keys.sumOf {
            it.price * (_cartProducts[it] ?: 0)
        }

    val sumToPay
        get() = sumOfCartProducts +
                if (_deliveryInfo.value.isDelivery) {
                    (_deliveryInfo.value.cities as? Status.Success)
                        ?.data
                        ?.find { it.id == _deliveryInfo.value.address.city?.id }
                        ?.price
                        ?: 0
                } else 0

    fun addCartProduct(product: SingleProduct) {
        _cartProducts[product] = _cartProducts.getOrDefault(product, 0) + 1
        saveCart()
    }

    fun removeCartProduct(product: SingleProduct) {
        if (_cartProducts.containsKey(product)) {
            if (_cartProducts[product]!! > 1) {
                _cartProducts[product] = _cartProducts[product]!! - 1
            } else {
                _cartProducts.remove(product)
            }
        }
        saveCart()
    }

    fun removeCartProducts(product: SingleProduct) {
        _cartProducts.remove(product)
        saveCart()
    }

    fun clearCart() {
        _cartProducts.clear()
        saveCart()
    }

    private fun saveCart() {
        with(sharedPreferences.edit()) {
            putString(
                CART_PRODUCTS_SHARED_PREF,
                gson.toJson(
                    _cartProducts.entries.associate { (product, count) -> product.id to count }
                )
            )
            apply()
        }
    }

    private fun loadCart() {
        val products = (_products.value as? Status.Success ?: return).data

        val json = sharedPreferences.getString(CART_PRODUCTS_SHARED_PREF, null) ?: return
        val type = object : TypeToken<Map<Long, Int>>() {}.type
        val idToCountMap: Map<Long, Int> = gson.fromJson(json, type) ?: return

        val cartMap = mutableMapOf<SingleProduct, Int>()
        products.forEach { product ->
            product.id.forEachIndexed { index, productId ->
                idToCountMap[productId]?.let { count ->
                    val singleProduct = product.toSingleProduct(index)
                    if (!singleProduct.stop) {
                        cartMap[singleProduct] = count
                    }
                }
            }
        }

        _cartProducts.clear()
        _cartProducts.putAll(cartMap)
        saveCart()
    }

    // AdditionalInfo

    private val _additionalInfo = mutableStateOf(AdditionalInfo())
    val additionalInfo get() = _additionalInfo

    val withGingerWasabi get() = cartProducts.keys.any { it.putGingerAndWasabi }
    val withSticks get() = cartProducts.keys.any { it.putSticks }

    fun editPutGinger() {
        _additionalInfo.value =
            _additionalInfo.value.copy(ginger = !_additionalInfo.value.ginger)
    }

    fun editPutWasabi() {
        _additionalInfo.value =
            _additionalInfo.value.copy(wasabi = !_additionalInfo.value.wasabi)
    }

    fun addSticks() {
        if (_additionalInfo.value.sticks < 10) {
            _additionalInfo.value =
                _additionalInfo.value.copy(sticks = _additionalInfo.value.sticks + 1)
        }
    }

    fun minusSticks() {
        if (_additionalInfo.value.sticks > 0) {
            _additionalInfo.value =
                _additionalInfo.value.copy(sticks = _additionalInfo.value.sticks - 1)
        }
    }

    fun editDescr(newDescr: String) {
        _additionalInfo.value =
            _additionalInfo.value.copy(descr = newDescr)
    }

    // DeliveryInfo

    private val _deliveryInfo = mutableStateOf(DeliveryInfo())
    val deliveryInfo get() = _deliveryInfo

    fun getDeliveryCities() {
        _deliveryInfo.value = _deliveryInfo.value.copy(
            cities = Status.Loading
        )
        firestore.collection("points")
            .document((_pointDocumentId as Status.Success).data)
            .collection("delivery")
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                _deliveryInfo.value = _deliveryInfo.value.copy(
                    cities = Status.Success(querySnapshot.toObjects(Delivery::class.java))
                )
            }.addOnFailureListener { exception ->
                _deliveryInfo.value = _deliveryInfo.value.copy(cities = Status.Error(exception))
            }
    }

    fun editDeliveryMethod() {
        _deliveryInfo.value =
            _deliveryInfo.value.copy(isDelivery = !_deliveryInfo.value.isDelivery)
    }

    fun editDeliveryAddress(address: Address) {
        _deliveryInfo.value = _deliveryInfo.value.copy(address = address)
    }

    fun saveAddress() {
        with(sharedPreferences.edit()) {
            putString(
                DELIVERY_ADDRESS_SHARED_PREF,
                gson.toJson(_deliveryInfo.value.address)
            )
            apply()
        }
    }

    fun loadSavedAddress() {
        val json = sharedPreferences.getString(DELIVERY_ADDRESS_SHARED_PREF, null)
        if (!json.isNullOrEmpty()) {
            val address = gson.fromJson(json, Address::class.java)
            _deliveryInfo.value = _deliveryInfo.value.copy(
                savedAddress = if (address.city == null || address.street.isBlank() || address.home.isBlank()) {
                    null
                } else {
                    address
                }
            )
        }
    }

    fun hideLoadAddress() {
        _deliveryInfo.value = _deliveryInfo.value.copy(showLoadAddress = false)
    }

    // UserInfo

    private val _userInfo = mutableStateOf(UserInfo())
    val userInfo get() = _userInfo

    fun editUser(user: User) {
        _userInfo.value = _userInfo.value.copy(user = user)
    }

    fun saveUser() {
        with(sharedPreferences.edit()) {
            putString(
                USER_INFO_SHARED_PREF,
                gson.toJson(_userInfo.value.user)
            )
            apply()
        }
    }

    fun loadSavedUser() {
        val json = sharedPreferences.getString(USER_INFO_SHARED_PREF, null)
        if (!json.isNullOrEmpty()) {
            _userInfo.value =
                _userInfo.value.copy(savedUser = gson.fromJson(json, User::class.java))
        }
    }

    fun hideLoadUserInfo() {
        _userInfo.value = _userInfo.value.copy(showLoadUser = false)
    }

    // MoneyInfo

    private val _moneyInfo = mutableStateOf(MoneyInfo())
    val moneyInfo = _moneyInfo

    val change get() = (_moneyInfo.value.inputtedMoney.toIntOrNull() ?: 0) - sumToPay

    fun editPaymentMethod() {
        _moneyInfo.value = _moneyInfo.value.copy(isCash = !_moneyInfo.value.isCash)
    }

    fun editUserMoney(money: String) {
        _moneyInfo.value = _moneyInfo.value.copy(inputtedMoney = money)
    }

    // MailSender

    private val _mailAboutNewOrder = mutableStateOf<Status<String>>(Status.Pending)
    val mailAboutNewOrder get() = _mailAboutNewOrder

    private fun sendEmailAboutNewOrder(
        subject: String,
        message: String
    ) {
        _mailAboutNewOrder.value = Status.Loading
        if (_selectedPoint.value is Status.Success) {
            val point = (_selectedPoint.value as Status.Success).data
            try {
                val stringHost = "smtp.gmail.com"

                val properties = System.getProperties()
                properties["mail.smtp.host"] = stringHost
                properties["mail.smtp.port"] = "465"
                properties["mail.smtp.ssl.enable"] = "true"
                properties["mail.smtp.auth"] = "true"

                val session = Session.getInstance(properties, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(point.mailSender, point.mailPassword)
                    }
                })

                val mimeMessage = MimeMessage(session)

                mimeMessage.addRecipient(
                    Message.RecipientType.TO,
                    InternetAddress(point.mailRecipient)
                )

                mimeMessage.subject = subject
                mimeMessage.setText(message)

                val t = Thread {
                    try {
                        Transport.send(mimeMessage)
                        _mailAboutNewOrder.value = Status.Success("success")
                        clearCart()
                    } catch (e: MessagingException) {
                        _mailAboutNewOrder.value = Status.Error(e)
                    }
                }
                t.start()
            } catch (e: AddressException) {
                _mailAboutNewOrder.value = Status.Error(e)
            } catch (e: MessagingException) {
                _mailAboutNewOrder.value = Status.Error(e)
            }
        } else {
            _mailAboutNewOrder.value = Status.Error(Exception("Точка не выбрана."))
        }
    }

    private fun getOrderSummaryMessage(): String {
        val stringBuilder = StringBuilder()

        cartProducts.keys.forEach { product ->
            val count = cartProducts[product]
            stringBuilder.append(
                "${product.name} (Артикул = ${product.id}): $count\n"
            )
        }

        stringBuilder.append("\n")
        if (_deliveryInfo.value.isDelivery) {
            stringBuilder.append("Доставка: ${_deliveryInfo.value.address.toStringAddress()}\n\n")
        } else {
            stringBuilder.append("Самовывоз\n\n")
        }

        if (withSticks) {
            stringBuilder.append("Приборы: ${_additionalInfo.value.sticks}\n\n")
        }

        if (withGingerWasabi) {
            stringBuilder.append("Имбирь: ${if (_additionalInfo.value.ginger) "Да" else "Нет"}\n")
            stringBuilder.append("Васаби: ${if (_additionalInfo.value.wasabi) "Да" else "Нет"}\n\n")
        }

        val paymentMethod = if (_moneyInfo.value.isCash) "Наличные" else "Карта/QR"
        stringBuilder.append("Способ оплаты: $paymentMethod\n")

        if (_moneyInfo.value.isCash) {
            stringBuilder.append("Клиент даст: ${_moneyInfo.value.inputtedMoney}, Сдача: $change\n")
        }

        stringBuilder.append("\n")

        if (_additionalInfo.value.descr.isNotBlank()) {
            stringBuilder.append("Комментарий: ${_additionalInfo.value.descr}\n\n")
        }

        stringBuilder.append("${_userInfo.value.user.phoneNumber} ${_userInfo.value.user.name}\n\n")

        return stringBuilder.toString()
    }

    // Order

    private val _newOrderStatus = mutableStateOf<Status<ApiResponseNewOrder>>(Status.Pending)
    val newOrderResult get() = _newOrderStatus

    fun setNewOrderResult(result: Status<ApiResponseNewOrder>) {
        _newOrderStatus.value = result
    }

    fun postNewOrder() {
        _newOrderStatus.value = Status.Loading

        val products = cartProducts.keys.map { it.id }.toMutableList()
        val productsKol = cartProducts.values.toMutableList()
        if (deliveryInfo.value.isDelivery) {
            products.add(deliveryInfo.value.address.city!!.id)
            productsKol.add(1)
        }

        val call = frontpadApi.newOrder(
            secret = (selectedPoint.value as Status.Success).data.secretApi,
            product = products,
            productKol = productsKol,
            person = if (withSticks) additionalInfo.value.sticks else null,
            street = if (deliveryInfo.value.isDelivery) deliveryInfo.value.address.street else null,
            home = if (deliveryInfo.value.isDelivery) deliveryInfo.value.address.home else null,
            pod = if (deliveryInfo.value.isDelivery && deliveryInfo.value.address.pod.isNotBlank()) deliveryInfo.value.address.pod else null,
            et = if (deliveryInfo.value.isDelivery && deliveryInfo.value.address.et.isNotBlank()) deliveryInfo.value.address.et else null,
            apart = if (deliveryInfo.value.isDelivery && deliveryInfo.value.address.apart.isNotBlank()) deliveryInfo.value.address.apart else null,
            phone = userInfo.value.user.phoneNumber,
            name = userInfo.value.user.name.ifBlank { null },
            descr = (if (additionalInfo.value.ginger) " " else "без имбиря; ") +
                    (if (additionalInfo.value.wasabi) " " else "без васаби; ") +
                    (if (moneyInfo.value.isCash) "нал (${moneyInfo.value.inputtedMoney}:${change}); " else "карта; ") +
                    additionalInfo.value.descr,

            productMod = null,
            productPrice = null,
            score = null,
            sale = null,
            saleAmount = null,
            card = null,
            mail = null,
            pay = null,
            certificate = null,
            tags = null,
            hookStatus = null,
            hookUrl = null,
            channel = null,
            datetime = null,
            affiliate = null,
            point = null,
        )

        call.enqueue(object : Callback<ApiResponseNewOrder> {
            override fun onResponse(
                call: Call<ApiResponseNewOrder>,
                response: Response<ApiResponseNewOrder>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.result == "success") {
                        _newOrderStatus.value = Status.Success(response.body()!!)
                        sendEmailAboutNewOrder(
                            message = getOrderSummaryMessage(),
                            subject = "Новый заказ: ${response.body()?.orderId}",
                        )
                        clearCart()
                    } else if (response.body()?.result == "error") {
                        _newOrderStatus.value =
                            Status.Error(Exception(response.body()?.error.toString()))
                        sendEmailAboutNewOrder(
                            message = "${response.body()?.error}\n\n\n\n\n" + getOrderSummaryMessage(),
                            subject = "ОШИБКА заказа",
                        )
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Неизвестная ошибка"
                    _newOrderStatus.value = Status.Error(Exception(errorBody))
                    sendEmailAboutNewOrder(
                        message = "$errorBody\n\n\n\n\n" + getOrderSummaryMessage(),
                        subject = "ОШИБКА заказа",
                    )
                }
            }

            override fun onFailure(call: Call<ApiResponseNewOrder>, t: Throwable) {
                _newOrderStatus.value = Status.Error(Exception(t))
                sendEmailAboutNewOrder(
                    message = "${t.message}\n${t.stackTrace}\n\n\n\n\n" + getOrderSummaryMessage(),
                    subject = "ОШИБКА заказа",
                )
            }
        })
    }

    // topBarSearchRequestText

    private val _topBarSearchRequestText = mutableStateOf("")
    val topBarSearchRequestText get() = _topBarSearchRequestText

    fun updateTopBarSearchRequestText(searchRequest: String) {
        _topBarSearchRequestText.value = searchRequest
    }

    // Guide

    private val _isFirstEntrance = mutableStateOf(false)
    val isFirstEntrance get() = _isFirstEntrance

    fun saveIsFirstEntrance() {
        _isFirstEntrance.value = false
        with(sharedPreferences.edit()) {
            putBoolean(
                IS_FIRST_ENTRANCE_SHARED_PREF,
                _isFirstEntrance.value
            )
            apply()
        }
    }

    private fun loadIsFirstEntrance() {
        _isFirstEntrance.value = sharedPreferences.getBoolean(IS_FIRST_ENTRANCE_SHARED_PREF, true)
    }

    init {
        loadPointDocumentId(onComplete = {
            setPointListener(onComplete = {
                setProductsListener()
                getCategories()
                pointOpenChecker()
            })
        })
        loadIsFirstEntrance()
    }
}
