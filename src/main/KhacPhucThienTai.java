package main;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.TreeMap;

import trangthai.CapNganSach;
import trangthai.NhomNguon;

/*
 * KhacPhucThienTai: phần "khắc phục" của MỘT thiên tai.
 * Ở giai đoạn 3 lớp này chỉ lo phía THU: giữ các khoản kinh phí đã huy động được.
 * (Phía CHI - dùng tiền vào việc gì - sẽ thêm ở giai đoạn 4.)
 *
 * QUAN HỆ VỚI ThienTai (cách B, quan hệ 1-1, hai chiều):
 *  - ThienTai giữ đúng một KhacPhucThienTai (thuộc tính khacPhuc, tạo ngay trong constructor ThienTai).
 *  - KhacPhucThienTai giữ ngược lại thienTai của nó.
 *  Giống cách HoDan <-> KhuVuc đã làm. Xóa thiên tai thì khắc phục đi theo, không cần dọn riêng.
 *
 * Constructor để package-private (không có chữ public): chỉ ThienTai (cùng package) được tạo,
 * để không ai tạo ra một KhacPhucThienTai "mồ côi" hoặc tạo hai cái cho cùng một thiên tai.
 */
public class KhacPhucThienTai {

    // Thiên tai mà phần khắc phục này thuộc về. final: gán một lần, không đổi.
    private final ThienTai thienTai;

    // Các khoản kinh phí (mỗi phần tử = một lần góp), khóa là maNguon.
    // TreeMap: tra theo mã nhanh, đồng thời duyệt luôn theo thứ tự mã nên kết quả in ra ổn định.
    private TreeMap<String, NguonKinhPhi> dsNguonKinhPhi = new TreeMap<>();

    // Chỉ ThienTai gọi (truyền this vào).
    KhacPhucThienTai(ThienTai thienTai) {
        if (thienTai == null) {
            throw new IllegalArgumentException("Thiên tai không hợp lệ");
        }
        this.thienTai = thienTai;
    }

    //QUẢN LÝ NGUỒN KINH PHÍ

    /*
     * Thêm một khoản kinh phí. Trả về false nếu khoản null, mã nguồn đã có,
     * nhóm Nhà nước nhưng không phải NguonNhaNuoc, hoặc bên đó đã có ở nhóm khác.
     * Không chặn khi thiên tai đã kết thúc: việc quyên góp thường còn tiếp tục
     * trong lúc khắc phục hậu quả, kể cả sau khi thiên tai chấm dứt.
     */
    public boolean themNguon(NguonKinhPhi nguon) {

        if (nguon == null) {
            return false;
        }

        // Mã nguồn là mã của MỘT lần góp nên phải duy nhất (khác maBen, một bên có thể góp nhiều lần)
        if (dsNguonKinhPhi.containsKey(nguon.getMaNguon())) {
            return false;
        }

        // Nhóm Nhà nước phải là NguonNhaNuoc (mới có cấp ngân sách)
        if (nguon.getNhom() == NhomNguon.NHA_NUOC && !(nguon instanceof NguonNhaNuoc)) {
            return false;
        }

        // Một bên (maBen) chỉ được thuộc một nhóm, trái nhóm thì từ chối ngay
        for (NguonKinhPhi n : dsNguonKinhPhi.values()) {
            if (n.getMaBen().equals(nguon.getMaBen()) && n.getNhom() != nguon.getNhom()) {
                return false;
            }
        }

        dsNguonKinhPhi.put(nguon.getMaNguon(), nguon);
        return true;
    }

    // Xóa một khoản theo mã nguồn (ví dụ nhập nhầm). Trả về false nếu không có.
    public boolean xoaNguon(String maNguon) {

        if (maNguon == null || maNguon.trim().isEmpty()) {
            return false;
        }

        return dsNguonKinhPhi.remove(maNguon) != null;
    }

    // Tìm một khoản theo mã nguồn; không có thì trả null.
    public NguonKinhPhi timNguon(String maNguon) {

        if (maNguon == null || maNguon.trim().isEmpty()) {
            return null;
        }

        return dsNguonKinhPhi.get(maNguon);
    }

    // Số khoản (số lần góp) đang có.
    public int demSoNguon() {
        return dsNguonKinhPhi.size();
    }

    //TỔNG HỢP

    // Tổng kinh phí đã huy động được (cộng mọi khoản, mọi nhóm).
    public long tinhTongKinhPhi() {
        long tong = 0;
        for (NguonKinhPhi n : dsNguonKinhPhi.values()) {
            tong += n.getSoTien();
        }
        return tong;
    }

    /*
     * Tiền của nhà nước chia theo cấp ngân sách (Trung ương / Tỉnh).
     * Đủ mọi cấp, cấp chưa có khoản nào thì là 0.
     * Chỉ lấy các khoản thuộc nhóm NHA_NUOC; ép kiểu sang NguonNhaNuoc để lấy được cấp ngân sách
     * (chỉ lớp con NguonNhaNuoc mới có getCapNganSach).
     */
    public EnumMap<CapNganSach, Long> tongNhaNuocTheoCapNganSach() {

        EnumMap<CapNganSach, Long> kq = new EnumMap<>(CapNganSach.class);
        for (CapNganSach c : CapNganSach.values()) {
            kq.put(c, 0L);
        }

        for (NguonKinhPhi n : dsNguonKinhPhi.values()) {
            if (n instanceof NguonNhaNuoc) {
                NguonNhaNuoc nn = (NguonNhaNuoc) n;
                kq.put(nn.getCapNganSach(), kq.get(nn.getCapNganSach()) + n.getSoTien());
            }
        }

        return kq;
    }

    // Thống kê đóng góp hiện tại (bản chụp tại thời điểm gọi; muốn số mới thì gọi lại).
    public ThongKeDongGop thongKeDongGop() {
        return new ThongKeDongGop(new ArrayList<>(dsNguonKinhPhi.values()));
    }

    //GETTER

    public ThienTai getThienTai() {
        return thienTai;
    }

    // Trả về bản sao để bên ngoài không sửa trực tiếp danh sách bên trong
    // (muốn thêm/xóa thì dùng themNguon / xoaNguon để còn kiểm tra hợp lệ).
    public TreeMap<String, NguonKinhPhi> getDsNguonKinhPhi() {
        return new TreeMap<>(dsNguonKinhPhi);
    }
}