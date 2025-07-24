package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.room.Room;

import com.example.nikeshop.R;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.CategoryDao;
import com.example.nikeshop.data.local.dao.OrderDao;
import com.example.nikeshop.data.local.dao.ProductDao;
import com.example.nikeshop.data.local.dao.UserDao;
import com.example.nikeshop.data.local.entity.Category;
import com.example.nikeshop.data.local.entity.Order;
import com.example.nikeshop.data.local.entity.Product;
import com.example.nikeshop.data.local.entity.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CheckBox checkboxAdmin = findViewById(R.id.checkbox_admin);

        // Ánh xạ View
        EditText nameInput = findViewById(R.id.nameInput);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        EditText confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        Button signUpButton = findViewById(R.id.signUpButton);
        ImageView eyeIconPassword = findViewById(R.id.eyeIconPassword);
        ImageView eyeIconConfirmPassword = findViewById(R.id.eyeIconConfirmPassword);
        TextView signInLink = findViewById(R.id.signInLink);

        // DB
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "nike_shop_db").allowMainThreadQueries().build();
        UserDao userDao = db.userDao();

        // Icon hiện/ẩn mật khẩu
        setupEyeToggle(eyeIconPassword, passwordInput);
        setupEyeToggle(eyeIconConfirmPassword, confirmPasswordInput);

        // Chuyển về Login
        signInLink.setOnClickListener(v -> {
            startActivity(new Intent(SignUp.this, LoginActivity.class));
            finish();
        });

        // Nút Đăng ký
        signUpButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showToast("Please fill in all fields");
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Please enter a valid email address");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showToast("Passwords do not match");
                return;
            }

            if (userDao.getUserByEmail(email) != null) {
                showToast("Email is already registered");
                return;
            }

            boolean isAdmin = checkboxAdmin.isChecked();

            // Lưu user
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPasswordHash(hashPassword(password));
            user.setAdmin(isAdmin);

            userDao.insertUser(user);

            if (user.isAdmin()) {
                seedSampleData(db);
            }

            showToast("Account created successfully!");
            startActivity(new Intent(SignUp.this, LoginActivity.class));
            finish();
        });
    }

    private void seedSampleData(AppDatabase db) {


        OrderDao orderDao = db.orderDao();
        CategoryDao categoryDao = db.categoryDao();
        ProductDao productDao = db.productDao();


        // Thêm đơn hàng
        orderDao.insert(new Order(1, new Date(), 250000, "pending", "COD", new Date(), null, null));


        if (categoryDao.getAllNow().isEmpty()) {
            categoryDao.insert(new Category("Giày thể thao", new Date(), new Date(), null));
            categoryDao.insert(new Category("Giày chạy bộ", new Date(), new Date(), null));
            categoryDao.insert(new Category("Giày đá bóng", new Date(), new Date(), null));
            categoryDao.insert(new Category("Giày tập gym", new Date(), new Date(), null));
            categoryDao.insert(new Category("Giày thời trang", new Date(), new Date(), null));
        }

        if (productDao.getAllNow().isEmpty()) {

        productDao.insert(new Product(
                "Nike Air Zoom Pegasus 39",
                "Giày chạy bộ với đệm Zoom Air êm ái, nhẹ và bền.",
                3200000,
                "42",
                10,
                "https://runningstore.vn/wp-content/uploads/2022/07/87825e12da3a1864412b.jpg",
                2, // Giày chạy bộ
                "Nike",
                "Đen/Trắng",
                "Vải Mesh",
                "NKPGS39",
                new Date(), new Date(), null
        ));

        productDao.insert(new Product(
                "Adidas Predator Accuracy.3 FG",
                "Giày đá bóng sân cỏ tự nhiên với thiết kế vân 3D giúp kiểm soát bóng tốt hơn.",
                2400000,
                "43",
                5,
                "https://sneakerdaily.vn/wp-content/uploads/2024/08/Giay-adidas-Predator-Accuracy.3-Low-FG-Heatspawn-Pack-GW4601.jpg",
                3, // Giày đá bóng
                "Adidas",
                "Trắng/Đỏ",
                "Synthetic",
                "ADPRED3",
                new Date(), new Date(), null
        ));

        productDao.insert(new Product(
                "Nike Metcon 8",
                "Giày tập gym siêu bền, hỗ trợ nâng tạ và cardio, đế phẳng tăng độ ổn định.",
                2900000,
                "41",
                12,
                "https://kallos.co/cdn/shop/files/8-amp-_-_-ldD3jNqM_61bf3297-3433-43b0-a9f4-afbca48c7217.jpg?v=1692835580&width=1080",                                                  4, // Giày tập gym
                "Nike",
                "Xám/Đen",
                "Vải tổng hợp",
                "NKMC8",
                new Date(), new Date(), null
        ));

        productDao.insert(new Product(
                "Converse Chuck Taylor All Star",
                "Giày thời trang cổ điển, dễ phối đồ, phù hợp đi học, đi chơi.",
                1500000,
                "40",
                20,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjKTtIAAo93BTqIdVLoJ79CYRA9bHc26sTEg&s",                                                  5, // Giày thời trang
                "Converse",
                "Trắng",
                "Canvas",
                "CVCHUCK1",
                new Date(), new Date(), null
        ));

        productDao.insert(new Product(
                "Puma Velocity Nitro 2",
                "Giày chạy nhẹ, độ nảy cao nhờ đệm Nitro độc quyền.",
                2700000,
                "42",
                8,
                "https://bizweb.dktcdn.net/thumb/1024x1024/100/399/577/products/image-1687945845771.png",                                                  2, // Giày chạy bộ
                "Puma",
                "Xanh navy",
                "Vải lưới",
                "PMVLN2",
                new Date(), new Date(), null
        ));
        }
    }

    private void setupEyeToggle(ImageView eyeIcon, EditText passwordField) {
        eyeIcon.setOnClickListener(new View.OnClickListener() {
            boolean isVisible = false;
            @Override
            public void onClick(View v) {
                isVisible = !isVisible;
                if (isVisible) {
                    passwordField.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordField.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                passwordField.setSelection(passwordField.length());
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(SignUp.this, msg, Toast.LENGTH_SHORT).show();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
