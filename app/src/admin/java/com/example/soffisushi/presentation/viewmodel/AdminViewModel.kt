package com.example.soffisushi.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.soffisushi.data.remote.firebase.model.Category
import com.example.soffisushi.data.remote.firebase.model.Delivery
import com.example.soffisushi.data.remote.firebase.model.Point
import com.example.soffisushi.data.remote.firebase.model.Product
import com.example.soffisushi.data.remote.firebase.model.Stock
import com.example.soffisushi.util.Status
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot

class AdminViewModel(
    val firestore: FirebaseFirestore
) : ViewModel() {
    // Point
    private val _points: MutableState<Status<QuerySnapshot>> = mutableStateOf(Status.Pending)
    val points get() = _points

    private val _selectedPointId: MutableState<String?> = mutableStateOf(null)
    val selectedPointId get() = _selectedPointId

    fun setSelectedPointId(id: String?) {
        _selectedPointId.value = id
    }

    private var _pointsRegistration: ListenerRegistration? = null
    fun setPointsListener(onSuccess: () -> Unit = {}) {
        _pointsRegistration?.remove()
        _points.value = Status.Loading
        _pointsRegistration = firestore.collection("points")
            .addSnapshotListener { documentSnapshot, exception ->
                if (exception != null) {
                    _points.value = Status.Error(exception)
                    return@addSnapshotListener
                }

                if (documentSnapshot != null) {
                    _points.value = Status.Success(documentSnapshot)
                    onSuccess()
                } else {
                    _points.value =
                        Status.Error(Exception("setPointsListener(): documentSnapshot == null"))
                }
            }
    }

    fun createPoint(
        id: String,
        point: Point,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        firestore.collection("points")
            .document(id)
            .set(point)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun updatePoint(
        id: String,
        point: Point,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val pointData = mapOf(
            "address" to point.address,
            "open" to point.open,
            "timeZone" to point.timeZone,
            "startTime" to point.startTime,
            "endTime" to point.endTime,
            "secretApi" to point.secretApi,
            "mailSender" to point.mailSender,
            "mailPassword" to point.mailPassword,
            "mailRecipient" to point.mailRecipient,
            "phoneNumber" to point.phoneNumber,
            "vkUrl" to point.vkUrl
        )

        firestore.collection("points")
            .document(id)
            .update(pointData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }


    fun deletePoint(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        firestore.collection("points")
            .document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }

    }

    // Products

    private val _products: MutableState<Status<QuerySnapshot>> = mutableStateOf(Status.Pending)
    val products get() = _products

    private val _selectedProductId: MutableState<String?> = mutableStateOf(null)
    val selectedProductId get() = _selectedProductId

    fun setSelectedProductId(id: String?) {
        _selectedProductId.value = id
    }

    private var _productsRegistration: ListenerRegistration? = null
    fun setProductsListener(onSuccess: () -> Unit = {}) {
        _productsRegistration?.remove()
        if (_selectedPointId.value == null) {
            _products.value =
                Status.Error(Exception("setProductsListener(): selectedPointId = null"))
            return
        }
        _products.value = Status.Loading
        _productsRegistration = firestore.collection("points")
            .document(_selectedPointId.value!!)
            .collection("products")
            .addSnapshotListener { documentSnapshot, exception ->
                if (exception != null) {
                    _products.value = Status.Error(exception)
                    return@addSnapshotListener
                }

                if (documentSnapshot != null) {
                    _products.value = Status.Success(documentSnapshot)
                    onSuccess()
                } else {
                    _products.value =
                        Status.Error(Exception("setProductsListener(): documentSnapshot == null"))
                }
            }
    }

    fun createProduct(
        id: String,
        product: Product,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        firestore.collection("points")
            .document(_selectedPointId.value!!)
            .collection("products")
            .document(id)
            .set(product)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun deleteProduct(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        if (_selectedPointId.value == null) {
            onFailure(Exception("deleteProduct(): selectedPointId = null"))
            return
        }
        firestore.collection("points")
            .document(_selectedPointId.value!!)
            .collection("products")
            .document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }

    }

    fun updateCategoryInProducts(
        oldCategoryId: Long,
        newCategoryId: Long,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val pointId = _selectedPointId.value
        if (pointId == null) {
            onFailure(Exception("updateCategoryInProducts(): selectedPointId is null"))
            return
        }

        firestore.collection("points")
            .document(pointId)
            .collection("products")
            .whereEqualTo("categoryId", oldCategoryId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val batch = firestore.batch()

                for (document in querySnapshot.documents) {
                    val productRef = document.reference
                    batch.update(productRef, "categoryId", newCategoryId)
                }

                batch.commit()
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { exception ->
                        onFailure(exception)
                    }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    // Categories

    private val _categories: MutableState<Status<QuerySnapshot>> = mutableStateOf(Status.Pending)
    val categories get() = _categories

    private val _selectedCategoryId: MutableState<String?> = mutableStateOf(null)
    val selectedCategoryId get() = _selectedCategoryId

    fun setSelectedCategoryId(id: String?) {
        _selectedCategoryId.value = id
    }

    private var _categoriesRegistration: ListenerRegistration? = null
    fun setCategoriesListener(onSuccess: () -> Unit = {}) {
        _categoriesRegistration?.remove()
        if (_selectedPointId.value == null) {
            _categories.value =
                Status.Error(Exception("setCategoriesListener(): selectedPointId = null"))
            return
        }
        _categories.value = Status.Loading
        _categoriesRegistration = firestore.collection("points")
            .document(_selectedPointId.value!!)
            .collection("categories")
            .addSnapshotListener { documentSnapshot, exception ->
                if (exception != null) {
                    _categories.value = Status.Error(exception)
                    return@addSnapshotListener
                }

                if (documentSnapshot != null) {
                    _categories.value = Status.Success(documentSnapshot)
                    onSuccess()
                } else {
                    _categories.value =
                        Status.Error(Exception("setCategoriesListener(): documentSnapshot == null"))
                }
            }
    }

    fun createCategory(
        id: String,
        category: Category,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        firestore.collection("points")
            .document(_selectedPointId.value!!)
            .collection("categories")
            .document(id)
            .set(category)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun deleteCategory(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        if (_selectedPointId.value == null) {
            onFailure(Exception("deleteCategory(): selectedPointId = null"))
            return
        }
        firestore.collection("points")
            .document(_selectedPointId.value!!)
            .collection("categories")
            .document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }

    }

    // Stocks

    private val _stocks: MutableState<Status<QuerySnapshot>> = mutableStateOf(Status.Pending)
    val stocks get() = _stocks

    private val _selectedStockId: MutableState<String?> = mutableStateOf(null)
    val selectedStockId get() = _selectedStockId

    fun setSelectedStockId(id: String?) {
        _selectedStockId.value = id
    }

    private var _stocksRegistration: ListenerRegistration? = null
    fun setStocksListener(onSuccess: () -> Unit = {}) {
        _stocksRegistration?.remove()
        if (_selectedPointId.value == null) {
            _categories.value =
                Status.Error(Exception("setStocksListener(): selectedPointId = null"))
            return
        }
        _stocks.value = Status.Loading
        _stocksRegistration = firestore.collection("points")
            .document(_selectedPointId.value!!)
            .collection("stocks")
            .addSnapshotListener { documentSnapshot, exception ->
                if (exception != null) {
                    _stocks.value = Status.Error(exception)
                    return@addSnapshotListener
                }

                if (documentSnapshot != null) {
                    _stocks.value = Status.Success(documentSnapshot)
                    onSuccess()
                } else {
                    _stocks.value =
                        Status.Error(Exception("setStocksListener(): documentSnapshot == null"))
                }
            }
    }

    fun createStock(
        id: String,
        stock: Stock,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        firestore.collection("points")
            .document(_selectedPointId.value!!)
            .collection("stocks")
            .document(id)
            .set(stock)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun deleteStock(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        if (_selectedPointId.value == null) {
            onFailure(Exception("deleteStock(): selectedPointId = null"))
            return
        }
        firestore.collection("points")
            .document(_selectedPointId.value!!)
            .collection("stocks")
            .document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }

    }

    // Deliveries

    private val _deliveries: MutableState<Status<QuerySnapshot>> = mutableStateOf(Status.Pending)
    val deliveries get() = _deliveries

    private val _selectedDeliveryId: MutableState<String?> = mutableStateOf(null)
    val selectedDeliveryId get() = _selectedDeliveryId

    fun setSelectedDeliveryId(id: String?) {
        _selectedDeliveryId.value = id
    }

    private var _deliveriesRegistration: ListenerRegistration? = null
    fun setDeliveriesListener(onSuccess: () -> Unit = {}) {
        _deliveriesRegistration?.remove()
        if (_selectedPointId.value == null) {
            _categories.value =
                Status.Error(Exception("setDeliveriesListener(): selectedPointId = null"))
            return
        }
        _deliveries.value = Status.Loading
        _deliveriesRegistration = firestore.collection("points")
            .document(_selectedPointId.value!!)
            .collection("delivery")
            .addSnapshotListener { documentSnapshot, exception ->
                if (exception != null) {
                    _deliveries.value = Status.Error(exception)
                    return@addSnapshotListener
                }

                if (documentSnapshot != null) {
                    _deliveries.value = Status.Success(documentSnapshot)
                    onSuccess()
                } else {
                    _deliveries.value =
                        Status.Error(Exception("setDeliveriesListener(): documentSnapshot == null"))
                }
            }
    }

    fun createDelivery(
        id: String,
        delivery: Delivery,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        firestore.collection("points")
            .document(_selectedPointId.value!!)
            .collection("delivery")
            .document(id)
            .set(delivery)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun deleteDelivery(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        if (_selectedPointId.value == null) {
            onFailure(Exception("deleteDelivery(): selectedPointId = null"))
            return
        }
        firestore.collection("points")
            .document(_selectedPointId.value!!)
            .collection("delivery")
            .document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // Other

    fun setPointListener() {
        setProductsListener()
        setCategoriesListener()
        setStocksListener()
        setDeliveriesListener()
    }

    init {
        setPointsListener()
    }
}