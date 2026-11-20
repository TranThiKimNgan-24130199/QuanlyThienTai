package main;
//người thực hiện: Nguyễn Thị Hoài Lan
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.TreeMap;

import trangthai.CapNganSach;
import trangthai.LoaiHoatDong;
import trangthai.MucDichSuDung;
import trangthai.NhomNguon;
import trangthai.TrangThaiKhacPhuc;

/*
 * KhacPhucThienTai: phần "khắc phục" của MỘT thiên tai.
 * Giai đoạn 3: phía THU - giữ các khoản kinh phí đã huy động được (nguồn kinh phí).
 * Giai đoạn 4: phía CHI - giữ các hoạt động khắc phục và ghi nhận chi từ từng nguồn.
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

    // Các hoạt động khắc phục (hỗ trợ hộ, sửa chữa / xây mới hạ tầng), khóa là maHoatDong.
    private TreeMap<String, HoatDongKhacPhuc> dsHoatDong = new TreeMap<>();

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
        // Khóa nhãn mục đích: người góp quyết định mục đích lúc góp, sau đó không đổi.
        // Vì vậy phải đặt mục đích TRƯỚC khi thêm nguồn vào khắc phục.
        nguon.khoaMucDich();
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

    // Xóa một khoản theo mã nguồn (ví dụ nhập nhầm). Trả về false nếu không có hoặc nguồn đã có chi.
    public boolean xoaNguon(String maNguon) {

        if (maNguon == null || maNguon.trim().isEmpty()) {
            return false;
        }

        // Nguồn đã có chi thì không xóa: xóa sẽ làm mất dấu số tiền đã chi từ nguồn đó
        if (daChiTuNguon(maNguon) > 0) {
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

// người thực hiện : Trần Thị Kim Ngân
    /*
     * Thêm một hoạt động khắc phục. Trả về false nếu:
     *  - hoạt động null hoặc không hợp lệ (kiemTraHopLe = false)
     *  - hoạt động đã có tiền chi (chi phải đi qua ghiNhanChi để được kiểm tra nguồn)
     *  - trùng mã hoạt động
     *  - maKV không thuộc thiên tai này
     *  - HO_TRO_HO_DAN: hộ chưa có bản ghi thiệt hại trong thiên tai này, hoặc hộ không thuộc khu vực maKV
     *  - hai loại hạ tầng: maCSVC không phải khoản thiệt hại cơ sở vật chất của khu vực đó
     *    ghi cho thiên tai này
     */
    public boolean themHoatDong(HoatDongKhacPhuc hd) {

        if (hd == null || !hd.kiemTraHopLe()) {
            return false;
        }

        if (hd.tongChi() > 0) {
            return false;
        }

        if (dsHoatDong.containsKey(hd.getMaHoatDong())) {
            return false;
        }

        // Khu vực thực hiện phải thuộc thiên tai này
        KhuVuc kv = thienTai.getDsKhuVuc().get(hd.getMaKV());
        if (kv == null) {
            return false;
        }

        if (hd.getLoai() == LoaiHoatDong.HO_TRO_HO_DAN) {
            // Hộ phải có bản ghi thiệt hại trong thiên tai này và thuộc đúng khu vực
            BanGhiThietHaiHoDan bg = timBanGhiCuaHo(hd.getMaHo());
            if (bg == null || bg.getHoDan().getKhuVuc() == null
                    || !hd.getMaKV().equals(bg.getHoDan().getKhuVuc().getMaKV())) {
                return false;
            }
        } else {
            // Hạ tầng: cơ sở vật chất phải là khoản thiệt hại của khu vực, ghi cho thiên tai này
            ThietHaiCoSoVatChat_NN cs = kv.timCoSoVatChat(hd.getMaCSVC());
            if (cs == null || !thienTai.getMaTT().equals(cs.getMaTT())) {
                return false;
            }
        }

        dsHoatDong.put(hd.getMaHoatDong(), hd);
        return true;
    }

    // Tìm một hoạt động theo mã; không có thì trả null.
    public HoatDongKhacPhuc timHoatDong(String maHoatDong) {

        if (maHoatDong == null || maHoatDong.trim().isEmpty()) {
            return null;
        }

        return dsHoatDong.get(maHoatDong);
    }

    // Xóa hoạt động theo mã. Trả về false nếu không có hoặc hoạt động đã chi đồng nào.
    public boolean xoaHoatDong(String maHoatDong) {

        HoatDongKhacPhuc hd = timHoatDong(maHoatDong);

        if (hd == null || hd.tongChi() > 0) {
            return false;
        }

        dsHoatDong.remove(maHoatDong);
        return true;
    }

    // Số hoạt động khắc phục đang có.
    public int demSoHoatDong() {
        return dsHoatDong.size();
    }

    /*
     * Ghi nhận chi một khoản từ một nguồn cho một hoạt động. Trả về false (không ghi gì) nếu:
     *  - hoạt động hoặc nguồn không tồn tại
     *  - số tiền <= 0
     *  - hoạt động đã HOAN_THANH
     *  - nguồn có mục đích cụ thể (khác KHONG_CHI_DINH) mà khác mục đích loại hoạt động yêu cầu
     *  - số tiền lớn hơn số còn lại của nguồn
     * Hợp lệ thì cộng vào chi của hoạt động (congChi) và trả về true.
     */
    public boolean ghiNhanChi(String maHoatDong, String maNguon, long soTien) {

        HoatDongKhacPhuc hd = timHoatDong(maHoatDong);
        NguonKinhPhi nguon = timNguon(maNguon);

        if (hd == null || nguon == null) {
            return false;
        }

        if (soTien <= 0) {
            return false;
        }

        if (hd.getTrangThai() == TrangThaiKhacPhuc.HOAN_THANH) {
            return false;
        }

        MucDichSuDung mucDich = nguon.getMucDichSuDung();
        if (mucDich != MucDichSuDung.KHONG_CHI_DINH && mucDich != hd.getLoai().mucDichYeuCau()) {
            return false;
        }

        if (soTien > conLaiCuaNguon(maNguon)) {
            return false;
        }

        hd.congChi(maNguon, soTien);
        return true;
    }

    // Tổng tiền đã chi từ một nguồn, cộng qua mọi hoạt động (nguồn chưa chi hoặc không có thì là 0).
    public long daChiTuNguon(String maNguon) {

        if (maNguon == null || maNguon.trim().isEmpty()) {
            return 0;
        }

        long tong = 0;
        for (HoatDongKhacPhuc hd : dsHoatDong.values()) {
            tong += hd.getChiTheoNguon().getOrDefault(maNguon, 0L);
        }
        return tong;
    }

    // Số tiền còn lại của một nguồn = tổng nguồn đã huy động - đã chi. Ném IllegalArgumentException nếu nguồn không tồn tại.
    public long conLaiCuaNguon(String maNguon) {

        NguonKinhPhi nguon = timNguon(maNguon);

        if (nguon == null) {
            throw new IllegalArgumentException("Nguồn kinh phí không tồn tại: " + maNguon);
        }

        return nguon.getTongSoTien() - daChiTuNguon(maNguon);
    }

    // Tổng tiền đã chi của mọi hoạt động.
    public long tongDaChi() {

        long tong = 0;
        for (HoatDongKhacPhuc hd : dsHoatDong.values()) {
            tong += hd.tongChi();
        }
        return tong;
    }

    // Kinh phí còn lại = tổng huy động - tổng đã chi (tinhTongKinhPhi vẫn là tổng huy động).
    public long tinhKinhPhiConLai() {
        return tinhTongKinhPhi() - tongDaChi();
    }

    // Tìm bản ghi thiệt hại của một hộ trong thiên tai này; không có thì trả null.
    private BanGhiThietHaiHoDan timBanGhiCuaHo(String maHo) {

        if (maHo == null) {
            return null;
        }

        for (BanGhiThietHaiHoDan bg : thienTai.getDsBanGhi().values()) {
            if (maHo.equals(bg.getHoDan().getMaHo())) {
                return bg;
            }
        }
        return null;
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

    // Bản sao danh sách hoạt động (muốn thêm/xóa thì dùng themHoatDong / xoaHoatDong).
    public TreeMap<String, HoatDongKhacPhuc> getDsHoatDong() {
        return new TreeMap<>(dsHoatDong);
    }
}