package main;
//người thực hiện : Trần Thị Kim Ngân
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import trangthai.LoaiHoatDong;
import trangthai.MucDapUng;

/*
 * DanhGiaHoTro: đánh giá mức đáp ứng hỗ trợ của MỘT thiên tai ở ba cấp: hộ dân, khu vực (xã/phường),
 * địa phương (tỉnh/thành phố, gộp các khu vực cùng tinhThanhPho).
 *
 * Mỗi cấp có ba số: nhu cầu (cần hỗ trợ bao nhiêu), đã hỗ trợ (đã chi bao nhiêu) và mức đáp ứng.
 * Lớp tính trực tiếp từ dữ liệu hiện tại của thiên tai (khắc phục, bản ghi hộ dân, khu vực) nên luôn mới nhất,
 * không chụp sẵn số liệu. Mã không tồn tại thì ném IllegalArgumentException (giống ThienTai.thongKeKhuVuc).
 */
public class DanhGiaHoTro {

    private final ThienTai thienTai;

    // Nhận thiên tai cần đánh giá; ném IllegalArgumentException nếu null.
    public DanhGiaHoTro(ThienTai thienTai) {
        if (thienTai == null) {
            throw new IllegalArgumentException("Thiên tai không hợp lệ");
        }
        this.thienTai = thienTai;
    }

    // ================= Hộ dân =================

    /*
     * Nhu cầu hỗ trợ của hộ = tổng các khoản theo thiệt hại ghi trong bản ghi của hộ
     * (do ChinhSachHoTro.tienHoTroHo tính). Ném IllegalArgumentException nếu hộ chưa có bản ghi trong thiên tai.
     */
    public long nhuCauHo(String maHo) {
        return ChinhSachHoTro.tienHoTroHo(timBanGhi(maHo));
    }

    // Số tiền đã hỗ trợ hộ = tổng chi của các hoạt động HO_TRO_HO_DAN có maHo đó.
    public long daHoTroHo(String maHo) {

        timBanGhi(maHo); // kiểm tra hộ tồn tại

        long tong = 0;
        for (HoatDongKhacPhuc hd : thienTai.getKhacPhuc().getDsHoatDong().values()) {
            if (hd.getLoai() == LoaiHoatDong.HO_TRO_HO_DAN && maHo.equals(hd.getMaHo())) {
                tong += hd.tongChi();
            }
        }
        return tong;
    }

    // Mức đáp ứng của hộ = chấm (nhu cầu hộ, đã hỗ trợ hộ).
    public MucDapUng mucDapUngHo(String maHo) {
        return MucDapUng.danhGia(nhuCauHo(maHo), daHoTroHo(maHo));
    }

    // ================= Khu vực =================

    /*
     * Nhu cầu của khu vực = tổng nhu cầu các hộ có bản ghi trong khu vực
     * + nhu cầu hạ tầng (tính từ mức thiệt hại của khu vực và giá trị thiệt hại cơ sở vật chất nhà nước của khu vực).
     * Ném IllegalArgumentException nếu khu vực không thuộc thiên tai.
     */
    public long nhuCauKhuVuc(String maKV) {

        timKhuVuc(maKV); // kiểm tra khu vực tồn tại

        long tong = 0;
        for (BanGhiThietHaiHoDan bg : thienTai.getDsBanGhi().values()) {
            if (thuocKhuVuc(bg, maKV)) {
                tong += ChinhSachHoTro.tienHoTroHo(bg);
            }
        }

        long giaTriHaTang = thienTai.thongKeKhuVuc(maKV).tongGiaTriCoSoVatChatNN();
        tong += ChinhSachHoTro.tinhNhuCauHaTang(thienTai.danhGiaKhuVuc(maKV), giaTriHaTang);
        return tong;
    }

    // Số tiền đã hỗ trợ khu vực = tổng chi mọi hoạt động có maKV đó (cả hỗ trợ hộ lẫn hạ tầng).
    public long daHoTroKhuVuc(String maKV) {

        timKhuVuc(maKV);

        long tong = 0;
        for (HoatDongKhacPhuc hd : thienTai.getKhacPhuc().getDsHoatDong().values()) {
            if (maKV.equals(hd.getMaKV())) {
                tong += hd.tongChi();
            }
        }
        return tong;
    }

    // Mức đáp ứng của khu vực = chấm (nhu cầu khu vực, đã hỗ trợ khu vực).
    public MucDapUng mucDapUngKhuVuc(String maKV) {
        return MucDapUng.danhGia(nhuCauKhuVuc(maKV), daHoTroKhuVuc(maKV));
    }

    // ================= Địa phương (tỉnh / thành phố) =================

    // Các tinhThanhPho có khu vực trong thiên tai, xếp theo tên.
    public List<String> danhSachDiaPhuong() {

        TreeSet<String> tinh = new TreeSet<>();
        for (KhuVuc kv : thienTai.getDsKhuVuc().values()) {
            tinh.add(kv.getTinhThanhPho());
        }
        return new ArrayList<>(tinh);
    }

    /*
     * Nhu cầu của địa phương = tổng nhu cầu các khu vực thuộc tỉnh
     * (không chấm lại mức thiệt hại cho cả tỉnh nên không cần thêm bảng ngưỡng).
     * Ném IllegalArgumentException nếu tỉnh không có khu vực nào trong thiên tai.
     */
    public long nhuCauDiaPhuong(String tinh) {

        long tong = 0;
        for (KhuVuc kv : khuVucCuaTinh(tinh)) {
            tong += nhuCauKhuVuc(kv.getMaKV());
        }
        return tong;
    }

    // Số tiền đã hỗ trợ địa phương = tổng đã hỗ trợ các khu vực thuộc tỉnh.
    public long daHoTroDiaPhuong(String tinh) {

        long tong = 0;
        for (KhuVuc kv : khuVucCuaTinh(tinh)) {
            tong += daHoTroKhuVuc(kv.getMaKV());
        }
        return tong;
    }

    // Mức đáp ứng của địa phương = chấm (nhu cầu địa phương, đã hỗ trợ địa phương).
    public MucDapUng mucDapUngDiaPhuong(String tinh) {
        return MucDapUng.danhGia(nhuCauDiaPhuong(tinh), daHoTroDiaPhuong(tinh));
    }

    // ================= Lọc theo mức đáp ứng =================

    // Các hộ có bản ghi trong thiên tai ở mức đáp ứng đã chọn, xếp theo mã hộ.
    public List<HoDan> danhSachHoTheoMucDapUng(MucDapUng mucDapUng) {

        kiemTraMuc(mucDapUng);

        List<HoDan> kq = new ArrayList<>();
        for (BanGhiThietHaiHoDan bg : thienTai.getDsBanGhi().values()) {
            if (mucDapUngHo(bg.getHoDan().getMaHo()) == mucDapUng) {
                kq.add(bg.getHoDan());
            }
        }
        kq.sort(Comparator.comparing(HoDan::getMaHo));
        return kq;
    }

    // Các khu vực của thiên tai ở mức đáp ứng đã chọn, xếp theo mã khu vực.
    public List<KhuVuc> danhSachKhuVucTheoMucDapUng(MucDapUng mucDapUng) {

        kiemTraMuc(mucDapUng);

        List<KhuVuc> kq = new ArrayList<>();
        for (KhuVuc kv : thienTai.getDsKhuVuc().values()) {
            if (mucDapUngKhuVuc(kv.getMaKV()) == mucDapUng) {
                kq.add(kv);
            }
        }
        kq.sort(Comparator.comparing(KhuVuc::getMaKV));
        return kq;
    }

    // Các địa phương (tỉnh/thành) ở mức đáp ứng đã chọn, xếp theo tên.
    public List<String> danhSachDiaPhuongTheoMucDapUng(MucDapUng mucDapUng) {

        kiemTraMuc(mucDapUng);

        List<String> kq = new ArrayList<>();
        for (String tinh : danhSachDiaPhuong()) {
            if (mucDapUngDiaPhuong(tinh) == mucDapUng) {
                kq.add(tinh);
            }
        }
        return kq;
    }

    // ================= hàm hỗ trợ nội bộ =================

    // Tìm bản ghi thiệt hại của hộ; không có thì ném IllegalArgumentException.
    private BanGhiThietHaiHoDan timBanGhi(String maHo) {

        if (maHo != null) {
            for (BanGhiThietHaiHoDan bg : thienTai.getDsBanGhi().values()) {
                if (maHo.equals(bg.getHoDan().getMaHo())) {
                    return bg;
                }
            }
        }
        throw new IllegalArgumentException("Hộ không có bản ghi trong thiên tai: " + maHo);
    }

    // Tìm khu vực của thiên tai; không có thì ném IllegalArgumentException.
    private KhuVuc timKhuVuc(String maKV) {

        KhuVuc kv = (maKV == null) ? null : thienTai.getDsKhuVuc().get(maKV);

        if (kv == null) {
            throw new IllegalArgumentException("Khu vực không thuộc thiên tai: " + maKV);
        }
        return kv;
    }

    // Các khu vực của thiên tai thuộc một tỉnh; không có khu vực nào thì ném IllegalArgumentException.
    private List<KhuVuc> khuVucCuaTinh(String tinh) {

        List<KhuVuc> kq = new ArrayList<>();
        for (KhuVuc kv : thienTai.getDsKhuVuc().values()) {
            if (kv.getTinhThanhPho().equals(tinh)) {
                kq.add(kv);
            }
        }

        if (kq.isEmpty()) {
            throw new IllegalArgumentException("Địa phương không có khu vực trong thiên tai: " + tinh);
        }
        return kq;
    }

    // Bản ghi có thuộc khu vực maKV không (so theo mã khu vực của hộ).
    private boolean thuocKhuVuc(BanGhiThietHaiHoDan bg, String maKV) {
        return bg.getHoDan().getKhuVuc() != null && maKV.equals(bg.getHoDan().getKhuVuc().getMaKV());
    }

    private void kiemTraMuc(MucDapUng mucDapUng) {
        if (mucDapUng == null) {
            throw new IllegalArgumentException("Mức đáp ứng không được null");
        }
    }
}

