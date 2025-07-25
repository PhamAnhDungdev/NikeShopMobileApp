🛍️ Nikeshop
Nikeshop là một ứng dụng mua sắm giày thể thao được xây dựng bằng Android (Java) theo kiến trúc MVVM, kết hợp với Room Database, LiveData, và ViewModel để đảm bảo hiệu năng và khả năng mở rộng.

🚀 Tính năng nổi bật
🔍 Duyệt danh sách sản phẩm với hình ảnh, mô tả, loại sản phẩm và giá.

🛒 Thêm vào giỏ hàng, điều chỉnh số lượng, xoá sản phẩm.

❤️ Yêu thích sản phẩm và quản lý danh sách yêu thích (wishlist).

💳 Thanh toán đơn hàng với tổng chi phí được tính tự động.

🏠 Trang chủ có menu điều hướng qua các màn như Home, Favourites, Cart, Profile.

🔐 Quản lý đăng nhập / phiên người dùng thông qua SharedPreferences.

📦 Lưu trữ dữ liệu cục bộ bằng Room Database.

🧱 Kiến trúc & Công nghệ
Thành phần	Mô tả
Java	Ngôn ngữ chính
Room DB	Lưu trữ sản phẩm, giỏ hàng, người dùng, wishlist
MVVM	Phân tách View, Logic, và Dữ liệu rõ ràng
LiveData	Quan sát dữ liệu thay đổi realtime
ViewModel	Quản lý trạng thái UI và dữ liệu logic
Glide	Load ảnh sản phẩm
SharedPreferences	Quản lý phiên đăng nhập người dùng

🏗️ Cấu trúc thư mục
bash
Sao chép
Chỉnh sửa
com.example.nikeshop
├── adapters/           # RecyclerView Adapter (Cart, Favourite, Product)
├── data/
│   ├── local/          # Room DB, DAO, Entities
│   └── modelDto/       # DTO cho các thực thể kết hợp (CartWithProduct, v.v.)
├── ui/
│   ├── activities/     # Các màn hình (CartActivity, FavouriteActivity, v.v.)
│   ├── fragments/      # (Nếu có)
│   └── ViewModels/     # CartViewModel, WishlistViewModel, v.v.
├── utils/              # Hàm tiện ích (User session, formatter, ...)
└── NikeShopApp.java    # Application class (nếu cần)
🧪 Hướng dẫn chạy
Clone project

bash
Sao chép
Chỉnh sửa
git clone https://github.com/yourusername/nikeshop.git
Mở trong Android Studio

Build project và chạy trên thiết bị/emulator

Tài khoản người dùng test:
Bạn có thể sửa/seed dữ liệu ban đầu trong Room hoặc thêm màn đăng ký đơn giản.

📸 Giao diện (optional)
Thêm ảnh chụp màn hình ứng dụng: Trang chủ, Giỏ hàng, Wishlist, Thanh toán...

📌 Gợi ý mở rộng
Tích hợp Firebase để lưu yêu thích online

Thêm tính năng đánh giá sản phẩm

Đăng nhập bằng Google

Giao diện tối (Dark mode)

Paging với danh sách sản phẩm lớn

🧑‍💻 Tác giả
👤 PhamAnhDung Cooperate The Team - PhamAnhDungdev
