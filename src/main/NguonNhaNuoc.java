package main;
//Người thực hiện : Trần Thị Kim Ngân
import trangthai.CapNganSach;
import trangthai.NhomNguon;

/* Nguồn kinh phí từ ngân sách Nhà nước (trung ương hoặc tỉnh)
 * Chỉ ghi nhận phần thu (đóng góp); không phân mức độ đóng góp cho nguồn này
 */
public class NguonNhaNuoc extends NguonKinhPhi {
    private CapNganSach capNganSach;

    //Tạo nguồn Nhà nước theo cấp ngân sách; ném IllegalArgumentException nếu cấp ngân sách null
    public NguonNhaNuoc(String maNguon, String tenNguon, String maTT, CapNganSach capNganSach) {
        super(maNguon, tenNguon, maTT);
        if (capNganSach == null) {
            throw new IllegalArgumentException("Cap ngan sach khong duoc null");
        }
        this.capNganSach = capNganSach;
    }

    //Hợp lệ khi dữ liệu chung hợp lệ và có cấp ngân sách
    @Override
    public boolean kiemTraHopLe() {
        return super.kiemTraHopLe() && capNganSach != null;
    }

    @Override
    public NhomNguon getNhomNguon() {
        return NhomNguon.NHA_NUOC;
    }

    public CapNganSach getCapNganSach() {
        return capNganSach;
    }
}
