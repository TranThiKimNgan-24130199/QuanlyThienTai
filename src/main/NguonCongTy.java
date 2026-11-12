package main;
//Người thực hiện : Trần Thị Kim Ngân
import trangthai.NhomNguon;

//Nguồn kinh phí do một công ty đóng góp, định danh bằng mã số thuế
public class NguonCongTy extends NguonKinhPhi {
    private final String maSoThue;

    //Tạo nguồn công ty; ném IllegalArgumentException nếu mã số thuế null hoặc rỗng
    public NguonCongTy(String maNguon, String tenNguon, String maTT, String maSoThue) {
        super(maNguon, tenNguon, maTT);
        if (!khongRong(maSoThue)) {
            throw new IllegalArgumentException("Ma so thue khong duoc rong");
        }
        this.maSoThue = maSoThue.trim();
    }

    //Hợp lệ khi dữ liệu chung hợp lệ và mã số thuế có dạng 10 chữ số hoặc 10 chữ số-3 chữ số
    @Override
    public boolean kiemTraHopLe() {
        return super.kiemTraHopLe() && maSoThue.matches("\\d{10}(-\\d{3})?");
    }

    @Override
    public NhomNguon getNhomNguon() {
        return NhomNguon.CONG_TY;
    }

    public String getMaSoThue() {
        return maSoThue;
    }
}
