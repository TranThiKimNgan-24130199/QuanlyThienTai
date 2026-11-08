package main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import trangthai.*;


public class Test {

    private static final DateTimeFormatter DINH_DANG_GIO_LAP = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {

        QuanLyThienTai quanLy = new QuanLyThienTai();

        // 1. Thiên tai và khu vực 
        ThienTai baoSo5 = new ThienTai("TT01", "Bão số 5", LoaiThienTai.BAO,
                LocalDateTime.of(2026, 10, 12, 6, 0), "Bão đổ bộ vào Đà Nẵng, mưa lớn kéo dài");
        KhuVuc thanhBinh = new KhuVuc("KV01", "Đà Nẵng", "Hải Châu", "Thanh Bình");

        batBuoc(quanLy.themThienTai(baoSo5), "thêm thiên tai TT01");
        batBuoc(quanLy.themKhuVuc(thanhBinh), "thêm khu vực KV01");
        batBuoc(baoSo5.themKhuVuc(thanhBinh), "gắn khu vực KV01 vào thiên tai TT01");

        // 2. Hai hộ dân, mỗi hộ có một chủ hộ 
        NguoiDan lan = new NguoiDan("079000000001", "Nguyễn Thị Hoài Lan", GioiTinh.NU,
                LocalDate.of(2006, 10, 15), TinhTrangHonNhan.DA_KET_HON, QuanHeChuHo.CHU_HO);
        NguoiDan ngan = new NguoiDan("079000000002", "Trần Thị Kim Ngân", GioiTinh.NU,
                LocalDate.of(2006, 7, 28), TinhTrangHonNhan.DOC_THAN, QuanHeChuHo.CHU_HO);

        HoDan hoLan = new HoDan("HD01", "12 Nguyễn Văn Linh", lan);
        HoDan hoNgan = new HoDan("HD02", "25 Lê Duẩn", ngan);

        batBuoc(quanLy.themHoDan("KV01", hoLan), "thêm hộ HD01 vào khu vực KV01");
        batBuoc(quanLy.themHoDan("KV01", hoNgan), "thêm hộ HD02 vào khu vực KV01");

        // 3. Thiệt hại của từng hộ 
        // Hộ Lan: chị Lan bị thương, một căn nhà hư hỏng
        BanGhiThietHaiHoDan bgLan = new BanGhiThietHaiHoDan("BG01", LocalDateTime.of(2026, 10, 13, 9, 30), hoLan);
        batBuoc(bgLan.capNhatTrangThaiNguoi(lan.getCCCD(), TrangThaiNguoi.BI_THUONG), "cập nhật trạng thái của Lan");

        ThietHaiNhaCua nhaLan = new ThietHaiNhaCua();
        nhaLan.capNhatVeCua(0, 1, 180_000_000L);
        batBuoc(bgLan.capNhatThietHaiTaiSan(nhaLan), "ghi thiệt hại nhà cửa hộ HD01");
        batBuoc(baoSo5.themBanGhi(bgLan), "thêm bản ghi BG01");

        // Hộ Ngân: không ai bị ảnh hưởng về người, mất một phần hoa màu và vật nuôi
        BanGhiThietHaiHoDan bgNgan = new BanGhiThietHaiHoDan("BG02", LocalDateTime.of(2026, 10, 13, 10, 15), hoNgan);

        ThietHaiSanXuat sanXuatNgan = new ThietHaiSanXuat();
        sanXuatNgan.capNhatSanXuat(0.5, 20, 30_000_000L);
        batBuoc(bgNgan.capNhatThietHaiTaiSan(sanXuatNgan), "ghi thiệt hại sản xuất hộ HD02");
        batBuoc(baoSo5.themBanGhi(bgNgan), "thêm bản ghi BG02");

        // 4. Thiệt hại cơ sở vật chất của nhà nước tại khu vực 
        ThietHaiCauDuong cauDuong = new ThietHaiCauDuong("CSVC01");
        cauDuong.capNhatCauDuong(1, 1, 1_500_000_000L);
        batBuoc(quanLy.themCoSoVatChat("KV01", cauDuong), "thêm thiệt hại cầu đường CSVC01");

        ThietHaiCongTrinhCong congTrinh = new ThietHaiCongTrinhCong("CSVC02");
        congTrinh.capNhatCongTrinh(2, 0, 450_000_000L);
        batBuoc(quanLy.themCoSoVatChat("KV01", congTrinh), "thêm thiệt hại công trình công CSVC02");

        //  5. Kết thúc thiên tai và lập báo cáo 
        batBuoc(baoSo5.ketThucSuKien(LocalDateTime.of(2026, 10, 14, 18, 0)), "kết thúc thiên tai TT01");

        BaoCao baoCao = new BaoCao("BC01", "Báo cáo thiệt hại Bão số 5 tại Thanh Bình", baoSo5, thanhBinh);
        inBaoCao(baoCao);
    }

    // In báo cáo thiệt hại của một thiên tai tại một khu vực
    private static void inBaoCao(BaoCao baoCao) {

        ThienTai thienTai = baoCao.getThienTai();
        KhuVuc khuVuc = baoCao.getKhuVuc();
        ThongKeThietHai tk = thienTai.thongKeKhuVuc(khuVuc.getMaKV());

        LocalDateTime ketThuc = thienTai.getThoiGianKetThuc();
        String denNgay = (ketThuc == null) ? "đang diễn ra" : ketThuc.toLocalDate().toString();

        System.out.println("Thời gian lập: " + baoCao.getThoiGianLap().format(DINH_DANG_GIO_LAP));
        System.out.println("Thiên tai: " + thienTai.getTen() + " (" + thienTai.getMaTT() + ")");
        System.out.println("Khu vực: " + khuVuc.getXaPhuong() + ", " + khuVuc.getQuanHuyen() + ", " + khuVuc.getTinhThanhPho());
        System.out.println("Thời gian: " + thienTai.getThoiGianBatDau().toLocalDate() + " đến " + denNgay);
        System.out.println("Số trường hợp thiệt hại về người: " + tk.tongSoNguoiBiAnhHuong());
        System.out.println("Thiệt hại về của cải người dân (VNĐ): " + dinhDangTien(tk.tongGiaTriTaiSanDan()));
        System.out.println("Thiệt hại cơ sở vật chất Nhà nước (VNĐ): " + dinhDangTien(tk.tongGiaTriCoSoVatChatNN()));
        System.out.println("Tổng thiệt hại (VNĐ): " + dinhDangTien(tk.tongGiaTriVatChat()));
        System.out.println("Mức độ thiệt hại khu vực: " + tenMucDo(thienTai.danhGiaKhuVuc(khuVuc.getMaKV())));

        // Đếm số hộ của khu vực này theo từng mức độ
        Map<MucDoThietHai, List<HoDan>> nhomHo = thienTai.nhomHoTheoMucDo();

        for (Map.Entry<MucDoThietHai, List<HoDan>> e : nhomHo.entrySet()) {
            int soHo = 0;
            for (HoDan ho : e.getValue()) {
                if (ho.getKhuVuc() == khuVuc) soHo++;
            }
            System.out.println("Số hộ mức " + tenMucDo(e.getKey()) + ": " + soHo);
        }

        System.out.println("Chi tiết từng hộ:");
        for (Map.Entry<MucDoThietHai, List<HoDan>> e : nhomHo.entrySet()) {
            for (HoDan ho : e.getValue()) {
                if (ho.getKhuVuc() == khuVuc) {
                    System.out.println("- " + ho.getMaHo() + " (chủ hộ " + ho.getChuHo().getHoTen() + "): "
                            + tenMucDo(e.getKey()));
                }
            }
        }

        // Danh sách cơ sở vật chất Nhà nước bị thiệt hại tại khu vực
        System.out.println("Cơ sở vật chất Nhà nước bị thiệt hại:");
        Map<String, ThietHaiCoSoVatChat_NN> dsCSVC = new TreeMap<>(khuVuc.getDsThietHaiCoSoVatChatNN());
        if (dsCSVC.isEmpty()) {
            System.out.println("- (không có)");
        }
        for (ThietHaiCoSoVatChat_NN csvc : dsCSVC.values()) {
            System.out.println("- Mã CSVC: " + csvc.getMaCSVC() + " | Loại: " + csvc.getLoaiCSVC()
                    + " | Thiệt hại (VNĐ): " + dinhDangTien(csvc.tinhTongThietHai()));
        }
    }

    // Dừng chương trình ngay nếu một bước dựng dữ liệu thất bại, tránh in ra báo cáo toàn số 0
    private static void batBuoc(boolean thanhCong, String buoc) {
        if (!thanhCong) {
            throw new IllegalStateException("Bước dựng dữ liệu thất bại: " + buoc);
        }
    }

    
    private static String dinhDangTien(long soTien) {
        return String.format(Locale.US, "%,d", soTien);
    }

    // Tên hiển thị của từng mức độ thiệt hại
    private static String tenMucDo(MucDoThietHai mucDo) {
        switch (mucDo) {
            case KHONG_DANG_KE: return "Không đáng kể";
            case NHE:           return "Nhẹ";
            case TRUNG_BINH:    return "Trung bình";
            case NANG:          return "Nặng";
            case RAT_NANG:      return "Rất nặng";
            default: throw new IllegalArgumentException("Mức độ không hợp lệ: " + mucDo);
        }
    }
}