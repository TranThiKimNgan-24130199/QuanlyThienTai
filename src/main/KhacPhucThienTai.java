package main;
//người thực hiện: Nguyễn Thị Hoài Lan
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
 */
public class KhacPhucThienTai {

    // Thiên tai mà phần khắc phục này thuộc về. final: gán một lần, không đổi.
    private final ThienTai thienTai;

    // Các nguồn kinh phí (mỗi phần tử = một bên đóng góp, đã cộng dồn các lần góp), khóa là maNguon.
    // TreeMap: tra theo mã nhanh, đồng thời duyệt luôn theo thứ tự mã nên kết quả in ra ổn định.
    private TreeMap<String, NguonKinhPhi> dsNguonKinhPhi = new TreeMap<>();

    // Chỉ ThienTai gọi (truyền this vào).
    KhacPhucThienTai(ThienTai thienTai) {
        if (thienTai == null) {
            throw new IllegalArgumentException("Thiên tai không hợp lệ");
        }
        this.thienTai = thienTai;
    }
    //quản lý nguồn kinh phí
    /*
     * Thêm một nguồn kinh phí (một bên đóng góp, các lần góp đã được cộng dồn trong nguồn).
     * Trả về false nếu:
     *  - nguồn null hoặc dữ liệu không hợp lệ (kiemTraHopLe = false, ví dụ CCCD không đủ 12 số)
     *  - nguồn chưa có đồng đóng góp nào (tổng tiền = 0): nếu cho vào thì thống kê sẽ lỗi khi chấm mức,
     *    và kéo sai bên thấp nhất, trung bình
     *  - nguồn thuộc thiên tai khác (maTT không trùng với thiên tai của khắc phục này)
     *  - mã nguồn đã có
     *  - nhóm Nhà nước nhưng không phải NguonNhaNuoc (không có cấp ngân sách)
     *  - cùng một cá nhân (CCCD) hoặc cùng một công ty (mã số thuế) đã có ở nguồn khác:
     *    một bên chỉ được có một nguồn, góp thêm thì gọi themDongGop trên nguồn đó
     * Không chặn khi thiên tai đã kết thúc: việc quyên góp thường còn tiếp tục
     * trong lúc khắc phục hậu quả, kể cả sau khi thiên tai chấm dứt.
     */
    public boolean themNguon(NguonKinhPhi nguon) {

        if (nguon == null || !nguon.kiemTraHopLe()) {
            return false;
        }

        if (nguon.getTongSoTien() <= 0) {
            return false;
        }

        // Nguồn phải đóng góp cho đúng thiên tai này
        if (!thienTai.getMaTT().equals(nguon.getMaTT())) {
            return false;
        }

        // Mã nguồn phải duy nhất trong thiên tai
        if (dsNguonKinhPhi.containsKey(nguon.getMaNguon())) {
            return false;
        }

        // Nhóm Nhà nước phải là NguonNhaNuoc (mới có cấp ngân sách)
        if (nguon.getNhomNguon() == NhomNguon.NHA_NUOC && !(nguon instanceof NguonNhaNuoc)) {
            return false;
        }

        // Cùng một bên không được xuất hiện ở hai nguồn (nếu không, mức đóng góp bị chia đôi)
        if (trungDinhDanh(nguon)) {
            return false;
        }

        dsNguonKinhPhi.put(nguon.getMaNguon(), nguon);
        return true;
    }

    // Kiểm tra bên đóng góp của nguồn này đã có ở một nguồn khác chưa (so CCCD cá nhân, mã số thuế công ty)
    private boolean trungDinhDanh(NguonKinhPhi nguon) {
        for (NguonKinhPhi n : dsNguonKinhPhi.values()) {
            if (nguon instanceof NguonCaNhan && n instanceof NguonCaNhan
                    && ((NguonCaNhan) nguon).getCCCD().equals(((NguonCaNhan) n).getCCCD())) {
                return true;
            }
            if (nguon instanceof NguonCongTy && n instanceof NguonCongTy
                    && ((NguonCongTy) nguon).getMaSoThue().equals(((NguonCongTy) n).getMaSoThue())) {
                return true;
            }
        }
        return false;
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

    // Số nguồn kinh phí (số bên đóng góp) đang có.
    public int demSoNguon() {
        return dsNguonKinhPhi.size();
    }
    //tổng hợp
    // Tổng kinh phí đã huy động được (cộng mọi khoản, mọi nhóm).
    public long tinhTongKinhPhi() {
        long tong = 0;
        for (NguonKinhPhi n : dsNguonKinhPhi.values()) {
            tong += n.getTongSoTien();
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
                kq.put(nn.getCapNganSach(), kq.get(nn.getCapNganSach()) + n.getTongSoTien());
            }
        }

        return kq;
    }

    // Thống kê đóng góp hiện tại (bản chụp tại thời điểm gọi; muốn số mới thì gọi lại).
    public ThongKeDongGop thongKeDongGop() {
        return new ThongKeDongGop(new ArrayList<>(dsNguonKinhPhi.values()));
    }
    //getter
    public ThienTai getThienTai() {
        return thienTai;
    }

    // Trả về bản sao để bên ngoài không sửa trực tiếp danh sách bên trong
    // (muốn thêm/xóa thì dùng themNguon / xoaNguon để còn kiểm tra hợp lệ).
    public TreeMap<String, NguonKinhPhi> getDsNguonKinhPhi() {
        return new TreeMap<>(dsNguonKinhPhi);
    }
}