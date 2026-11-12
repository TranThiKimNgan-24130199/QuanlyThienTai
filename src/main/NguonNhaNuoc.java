package main;
//Người thực hiện : Trần Thị Kim Ngân
import trangthai.CapNganSach;
import trangthai.NhomNguon;

//Nguồn kinh phí từ ngân sách Nhà nước (trung ương hoặc tỉnh), có theo dõi số tiền đã chi
public class NguonNhaNuoc extends NguonKinhPhi {
    private CapNganSach capNganSach;
    private long soTienDaChi;

    //Tạo nguồn Nhà nước theo cấp ngân sách; ném IllegalArgumentException nếu cấp ngân sách null
    public NguonNhaNuoc(String maNguon, String tenNguon, String maTT, CapNganSach capNganSach) {
        super(maNguon, tenNguon, maTT);
        if (capNganSach == null) {
            throw new IllegalArgumentException("Cap ngan sach khong duoc null");
        }
        this.capNganSach = capNganSach;
    }

    /*
     * Chi một khoản tiền từ nguồn này: cộng vào số tiền đã chi
     * Trả về false nếu số tiền không dương hoặc vượt quá số dư hiện có
     */
//    public boolean chiTien(long soTien) {
//        if (soTien <= 0 || soTien > tinhSoDu()) {
//            return false;
//        }
//        soTienDaChi += soTien;
//        return true;
//    }

    //Số dư còn lại = tổng số tiền đã đóng góp - số tiền đã chi
//    public long tinhSoDu() {
//        return tongSoTien - soTienDaChi;
//    }

    // Hợp lệ khi dữ liệu chung hợp lệ, có cấp ngân sách và 0 <= số đã chi <= tổng số tiền
//    @Override
//    public boolean kiemTraHopLe() {
//        return super.kiemTraHopLe() && capNganSach != null && soTienDaChi >= 0 && soTienDaChi <= tongSoTien;
//    }

    @Override
    public NhomNguon getNhomNguon() {
        return NhomNguon.NHA_NUOC;
    }

    public CapNganSach getCapNganSach() {
        return capNganSach;
    }

    public long getSoTienDaChi() {
        return soTienDaChi;
    }
}
