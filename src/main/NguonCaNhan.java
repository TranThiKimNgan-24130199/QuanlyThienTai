package main;
//Người thực hiện : Trần Thị Kim Ngân
import trangthai.NhomNguon;

// Nguồn kinh phí do một cá nhân đóng góp, định danh bằng số CCCD
public class NguonCaNhan extends NguonKinhPhi {
    private final String CCCD;

    //Tạo nguồn cá nhân; ném IllegalArgumentException nếu CCCD null hoặc rỗng
    public NguonCaNhan(String maNguon, String tenNguon, String maTT, String CCCD) {
        super(maNguon, tenNguon, maTT);
        if (!khongRong(CCCD)) {
            throw new IllegalArgumentException("CCCD khong duoc rong");
        }
        this.CCCD = CCCD.trim();
    }

    //Hợp lệ khi dữ liệu chung hợp lệ và CCCD gồm đúng 12 chữ số
    @Override
    public boolean kiemTraHopLe() {
        return super.kiemTraHopLe() && CCCD.matches("\\d{12}");
    }

    @Override
    public NhomNguon getNhomNguon() {
        return NhomNguon.CA_NHAN;
    }

    public String getCCCD() {
        return CCCD;
    }
}

