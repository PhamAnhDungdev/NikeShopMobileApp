<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nikeshop - README</title>
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            line-height: 1.6;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            color: #333;
            background-color: #f8f9fa;
        }
        
        .container {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        h1 {
            color: #2c3e50;
            border-bottom: 3px solid #3498db;
            padding-bottom: 10px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        h2 {
            color: #34495e;
            margin-top: 30px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        
        h3 {
            color: #2980b9;
            margin-top: 25px;
        }
        
        .emoji {
            font-size: 1.2em;
        }
        
        .feature-list {
            list-style: none;
            padding: 0;
        }
        
        .feature-list li {
            background: #ecf0f1;
            margin: 8px 0;
            padding: 10px 15px;
            border-radius: 5px;
            border-left: 4px solid #3498db;
        }
        
        .tech-table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        
        .tech-table th,
        .tech-table td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }
        
        .tech-table th {
            background-color: #3498db;
            color: white;
        }
        
        .tech-table tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        
        .code-block {
            background: #2c3e50;
            color: #ecf0f1;
            padding: 15px;
            border-radius: 5px;
            font-family: 'Courier New', monospace;
            overflow-x: auto;
            margin: 15px 0;
        }
        
        .folder-structure {
            background: #f8f9fa;
            border: 1px solid #dee2e6;
            padding: 15px;
            border-radius: 5px;
            font-family: 'Courier New', monospace;
            white-space: pre-line;
        }
        
        .author-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 10px;
            text-align: center;
            margin-top: 30px;
        }
        
        .suggestions {
            background: #fff3cd;
            border: 1px solid #ffeaa7;
            padding: 15px;
            border-radius: 5px;
            margin: 15px 0;
        }
        
        .suggestions ul {
            margin: 10px 0;
            padding-left: 20px;
        }
        
        .intro-text {
            background: #e8f5e8;
            padding: 15px;
            border-radius: 5px;
            border-left: 4px solid #27ae60;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>
            <span class="emoji">🛍️</span>
            Nikeshop
        </h1>
        
        <div class="intro-text">
            <p><strong>Nikeshop</strong> là một ứng dụng mua sắm giày thể thao được xây dựng bằng Android (Java) theo kiến trúc MVVM, kết hợp với Room Database, LiveData, và ViewModel để đảm bảo hiệu năng và khả năng mở rộng.</p>
        </div>

        <h2>
            <span class="emoji">🚀</span>
            Tính năng nổi bật
        </h2>
        
        <ul class="feature-list">
            <li><span class="emoji">🔍</span> Duyệt danh sách sản phẩm với hình ảnh, mô tả, loại sản phẩm và giá</li>
            <li><span class="emoji">🛒</span> Thêm vào giỏ hàng, điều chỉnh số lượng, xoá sản phẩm</li>
            <li><span class="emoji">❤️</span> Yêu thích sản phẩm và quản lý danh sách yêu thích (wishlist)</li>
            <li><span class="emoji">💳</span> Thanh toán đơn hàng với tổng chi phí được tính tự động</li>
            <li><span class="emoji">🏠</span> Trang chủ có menu điều hướng qua các màn như Home, Favourites, Cart, Profile</li>
            <li><span class="emoji">🔐</span> Quản lý đăng nhập / phiên người dùng thông qua SharedPreferences</li>
            <li><span class="emoji">📦</span> Lưu trữ dữ liệu cục bộ bằng Room Database</li>
        </ul>

        <h2>
            <span class="emoji">🧱</span>
            Kiến trúc & Công nghệ
        </h2>
        
        <table class="tech-table">
            <thead>
                <tr>
                    <th>Thành phần</th>
                    <th>Mô tả</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><strong>Java</strong></td>
                    <td>Ngôn ngữ chính</td>
                </tr>
                <tr>
                    <td><strong>Room DB</strong></td>
                    <td>Lưu trữ sản phẩm, giỏ hàng, người dùng, wishlist</td>
                </tr>
                <tr>
                    <td><strong>MVVM</strong></td>
                    <td>Phân tách View, Logic, và Dữ liệu rõ ràng</td>
                </tr>
                <tr>
                    <td><strong>LiveData</strong></td>
                    <td>Quan sát dữ liệu thay đổi realtime</td>
                </tr>
                <tr>
                    <td><strong>ViewModel</strong></td>
                    <td>Quản lý trạng thái UI và dữ liệu logic</td>
                </tr>
                <tr>
                    <td><strong>Glide</strong></td>
                    <td>Load ảnh sản phẩm</td>
                </tr>
                <tr>
                    <td><strong>SharedPreferences</strong></td>
                    <td>Quản lý phiên đăng nhập người dùng</td>
                </tr>
            </tbody>
        </table>

        <h2>
            <span class="emoji">🏗️</span>
            Cấu trúc thư mục
        </h2>
        
        <div class="folder-structure">com.example.nikeshop
├── adapters/           # RecyclerView Adapter (Cart, Favourite, Product)
├── data/
│   ├── local/          # Room DB, DAO, Entities
│   └── modelDto/       # DTO cho các thực thể kết hợp (CartWithProduct, v.v.)
├── ui/
│   ├── activities/     # Các màn hình (CartActivity, FavouriteActivity, v.v.)
│   ├── fragments/      # (Nếu có)
│   └── ViewModels/     # CartViewModel, WishlistViewModel, v.v.
├── utils/              # Hàm tiện ích (User session, formatter, ...)
└── NikeShopApp.java    # Application class (nếu cần)</div>

        <h2>
            <span class="emoji">🧪</span>
            Hướng dẫn chạy
        </h2>
        
        <h3>1. Clone project</h3>
        <div class="code-block">git clone https://github.com/yourusername/nikeshop.git</div>
        
        <h3>2. Mở trong Android Studio</h3>
        <p>Build project và chạy trên thiết bị/emulator</p>
        
        <h3>3. Tài khoản người dùng test</h3>
        <p>Bạn có thể sửa/seed dữ liệu ban đầu trong Room hoặc thêm màn đăng ký đơn giản.</p>

        <h2>
            <span class="emoji">📸</span>
            Giao diện (optional)
        </h2>
        
        <p>Thêm ảnh chụp màn hình ứng dụng: Trang chủ, Giỏ hàng, Wishlist, Thanh toán...</p>

        <h2>
            <span class="emoji">📌</span>
            Gợi ý mở rộng
        </h2>
        
        <div class="suggestions">
            <ul>
                <li>Tích hợp Firebase để lưu yêu thích online</li>
                <li>Thêm tính năng đánh giá sản phẩm</li>
                <li>Đăng nhập bằng Google</li>
                <li>Giao diện tối (Dark mode)</li>
                <li>Paging với danh sách sản phẩm lớn</li>
            </ul>
        </div>

        <div class="author-section">
            <h2>
                <span class="emoji">🧑‍💻</span>
                Tác giả
            </h2>
            <p><strong>👤 PhamAnhDung</strong></p>
            <p>Cooperate The Team - PhamAnhDungdev</p>
        </div>
    </div>
</body>
</html>
