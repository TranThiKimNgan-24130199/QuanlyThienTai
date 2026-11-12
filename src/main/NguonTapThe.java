package main;
//Người thực hiện : Trần Thị Kim Ngân
import trangthai.NhomNguon;

//Nguồn kinh phí do một tập thể (hội, đoàn thể, tổ chức…) đóng góp, có người đại diện
public class NguonTapThe extends NguonKinhPhi {
    private String nguoiDaiDien;

    //Tạo nguồn tập thể; ném IllegalArgumentException nếu người đại diện null hoặc rỗng
    public NguonTapThe(String maNguon, String tenNguon, String maTT, String nguoiDaiDien) {
        super(maNguon, tenNguon, maTT);
        if (!khongRong(nguoiDaiDien)) {
            throw new IllegalArgumentException("Nguoi dai dien khong duoc rong");
        }
        this.nguoiDaiDien = nguoiDaiDien.trim();
    }

    //Hợp lệ khi dữ liệu chung hợp lệ và có người đại diện
    @Override
    public boolean kiemTraHopLe() {
        return super.kiemTraHopLe() && khongRong(nguoiDaiDien);
    }

    @Override
    public NhomNguon getNhomNguon() {
        return NhomNguon.TAP_THE;
    }

    public String getNguoiDaiDien() {
        return nguoiDaiDien;
    }
}
