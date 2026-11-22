package main;
//người thực hiện: Nguyễn Thị Hoài Lan
import java.util.TreeMap;

import trangthai.LoaiHoatDong;
import trangthai.TrangThaiKhacPhuc;

/*
 * HoatDongKhacPhuc: MỘT hoạt động khắc phục thiên tai, thuộc một trong ba loại:
 * hỗ trợ một hộ dân, sửa chữa hạ tầng, hoặc xây mới hạ tầng (ví dụ làm đường mới).
 *
 * Lớp này chỉ GHI SỐ: hoạt động đã chi bao nhiêu và chi từ nguồn nào.
 * Việc kiểm tra nguồn có đủ tiền không, có đúng mục đích không là của
 * KhacPhucThienTai.ghiNhanChi (lớp đó biết các nguồn, còn hoạt động thì không).
 */
public class HoatDongKhacPhuc {

    private final String maHoatDong;
    private String ten;
    private final LoaiHoatDong loai;

    // Khu vực thực hiện (luôn bắt buộc)
    private final String maKV;

    // Chỉ dùng cho HO_TRO_HO_DAN: hộ được hỗ trợ. Các loại khác phải là null.
    private final String maHo;

    // Chỉ dùng cho hai loại hạ tầng: khoản thiệt hại cơ sở vật chất cần khắc phục.
    // HO_TRO_HO_DAN phải là null.
    private final String maCSVC;

    // Kinh phí dự kiến (VNĐ, lớn hơn 0). Chỉ để so sánh; chi thực tế có thể vượt.
    private long kinhPhiDuKien;

    private TrangThaiKhacPhuc trangThai = TrangThaiKhacPhuc.CHUA_BAT_DAU;

    // Số tiền đã chi, khóa là mã nguồn. TreeMap: duyệt theo mã nguồn nên kết quả in ra ổn định.
    private TreeMap<String, Long> chiTheoNguon = new TreeMap<>();

    /*
     * Tạo hoạt động mới (trạng thái CHUA_BAT_DAU, chưa chi đồng nào).
     * Ném IllegalArgumentException nếu:
     *  - mã hoạt động, tên, mã khu vực rỗng; loại null; kinh phí dự kiến <= 0
     *  - HO_TRO_HO_DAN mà maHo rỗng hoặc maCSVC khác null
     *  - loại hạ tầng mà maCSVC rỗng hoặc maHo khác null
     */
    public HoatDongKhacPhuc(String maHoatDong, String ten, LoaiHoatDong loai, String maKV,
            String maHo, String maCSVC, long kinhPhiDuKien) {

        if (khongRong(maHoatDong) == false || khongRong(ten) == false || khongRong(maKV) == false
                || loai == null || kinhPhiDuKien <= 0) {
            throw new IllegalArgumentException("Thong tin hoat dong khac phuc khong hop le");
        }

        if (loai == LoaiHoatDong.HO_TRO_HO_DAN) {
            if (khongRong(maHo) == false || maCSVC != null) {
                throw new IllegalArgumentException("Ho tro ho dan phai co maHo va khong co maCSVC");
            }
        } else {
            if (khongRong(maCSVC) == false || maHo != null) {
                throw new IllegalArgumentException("Hoat dong ha tang phai co maCSVC va khong co maHo");
            }
        }

        this.maHoatDong = maHoatDong;
        this.ten = ten;
        this.loai = loai;
        this.maKV = maKV;
        this.maHo = maHo;
        this.maCSVC = maCSVC;
        this.kinhPhiDuKien = kinhPhiDuKien;
    }

    // Đổi trạng thái. Cho đổi cả hai chiều (ví dụ HOAN_THANH về DANG_THUC_HIEN khi còn phải thanh toán thêm).
    public void capNhatTrangThai(TrangThaiKhacPhuc trangThaiMoi) {
        if (trangThaiMoi == null) {
            throw new IllegalArgumentException("Trang thai khong duoc null");
        }
        this.trangThai = trangThaiMoi;
    }

    /*
     * Ghi nhận thêm một khoản chi từ một nguồn, cộng dồn nếu nguồn đó đã chi cho hoạt động này.
     * Ném IllegalArgumentException nếu mã nguồn rỗng, số tiền <= 0 hoặc cộng vào sẽ tràn kiểu long.
     * Lưu ý: hàm này KHÔNG kiểm tra nguồn có tồn tại / đủ tiền / đúng mục đích.
     * Vì vậy để package-private: chỉ KhacPhucThienTai.ghiNhanChi được gọi (sau khi đã kiểm tra),
     * các nơi khác muốn ghi chi thì phải đi qua ghiNhanChi, không gọi trực tiếp hàm này.
     * (Hoạt động lấy từ timHoatDong là đối tượng thật nên gọi thẳng congChi sẽ qua mặt các kiểm tra.)
     */
    void congChi(String maNguon, long soTien) {
        if (khongRong(maNguon) == false || soTien <= 0) {
            throw new IllegalArgumentException("Khoan chi khong hop le");
        }

        long daChi = chiTheoNguon.getOrDefault(maNguon, 0L);
        // so sánh theo kiểu "MAX - soTien" để không bị tràn khi cộng
        if (daChi > Long.MAX_VALUE - soTien || tongChi() > Long.MAX_VALUE - soTien) {
            throw new IllegalArgumentException("Tong chi vuot gioi han");
        }

        chiTheoNguon.put(maNguon, daChi + soTien);
    }

    // Tổng số tiền đã chi cho hoạt động này (cộng mọi nguồn)
    public long tongChi() {
        long tong = 0;
        for (long t : chiTheoNguon.values()) {
            tong += t;
        }
        return tong;
    }

    // Kiểm tra dữ liệu còn nhất quán (dùng khi nhận hoạt động từ bên ngoài)
    public boolean kiemTraHopLe() {
        if (khongRong(maHoatDong) == false || khongRong(ten) == false || khongRong(maKV) == false
                || loai == null || trangThai == null || kinhPhiDuKien <= 0) {
            return false;
        }
        if (loai == LoaiHoatDong.HO_TRO_HO_DAN) {
            return khongRong(maHo) && maCSVC == null;
        }
        return khongRong(maCSVC) && maHo == null;
    }

    // Chuỗi khác null và không chỉ gồm khoảng trắng
    private static boolean khongRong(String s) {
        return s != null && !s.trim().isEmpty();
    }

    // getter
    public String getMaHoatDong() {
        return maHoatDong;
    }

    public String getTen() {
        return ten;
    }

    public LoaiHoatDong getLoai() {
        return loai;
    }

    public String getMaKV() {
        return maKV;
    }

    public String getMaHo() {
        return maHo;
    }

    public String getMaCSVC() {
        return maCSVC;
    }

    public long getKinhPhiDuKien() {
        return kinhPhiDuKien;
    }

    public TrangThaiKhacPhuc getTrangThai() {
        return trangThai;
    }

    // Trả về bản sao để bên ngoài không sửa trực tiếp số liệu chi (muốn thêm thì dùng congChi)
    public TreeMap<String, Long> getChiTheoNguon() {
        return new TreeMap<>(chiTheoNguon);
    }
}