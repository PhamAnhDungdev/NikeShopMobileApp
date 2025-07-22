

package com.example.nikeshop.ui.ViewModels;
import com.example.nikeshop.data.local.model.CartWithProduct;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.nikeshop.data.repositories.CartRepository;

public class CartViewModel extends AndroidViewModel {
    public void deleteCart(int cartId) {
        cartRepository.deleteCart(cartId);
    }
    public void updateCartQuantity(int cartId, int newQuantity) {
        cartRepository.updateCartQuantity(cartId, newQuantity);
    }
    public androidx.lifecycle.LiveData<java.util.List<CartWithProduct>> getCartWithProductByUser(int userId) {
        return cartRepository.getCartWithProductByUser(userId);
    }
    private final CartRepository cartRepository;

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepository = new CartRepository(application);
    }

    public void addToCart(int productId, int quantity) {
        android.content.SharedPreferences prefs = getApplication().getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        if (userId != -1) {
            cartRepository.addToCart(productId, quantity, userId);
        }
    }

}
