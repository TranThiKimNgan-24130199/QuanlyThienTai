package main;
//người thực hiện: Nguyễn Thị Hoài Lan & Trần Thị Kim Ngân
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import trangthai.*;

/*
 * Lớp test duy nhất: dựng dữ liệu mẫu cho Bão số 5 (TT01) rồi in 3 báo cáo chi tiết,
 *
 *
 * 1. Báo cáo thiệt hại: thông tin thiên tai, tổng hợp, chi tiết từng khu vực
 *    (cơ sở vật chất nhà nước, từng hộ dân, từng thành viên), danh sách theo mức độ.
 * 2. Báo cáo kinh phí đóng góp khắc phục: chi tiết từng nguồn, tổng hợp theo nhóm,
 *    kinh phí Nhà nước theo cấp ngân sách, bên cao nhất / thấp nhất / trung bình,
 *    mức đóng góp, so sánh với thiệt hại.
 * 3. Báo cáo hỗ trợ và chi khắc phục: các hoạt động khắc phục, chi từ nguồn nào, chi theo loại /
 *    khu vực / địa phương, nhu cầu - đã hỗ trợ - mức đáp ứng của hộ dân, khu vực, địa phương.
 */
public class Test {

    private static final DateTimeFormatter GIO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final LocalDateTime THOI_GIAN_GHI = LocalDateTime.of(2026, 10, 13, 9, 30);
   

    private static final QuanLyThienTai quanLy = new QuanLyThienTai();
    private static ThienTai baoSo5;

    public static void main(String[] args) {
        dungDuLieu();
        inBaoCao(baoSo5);
        inBaoCaoKinhPhi(baoSo5);
        inBaoCaoHoTro(baoSo5);
    }   
    //dữ liệu mẫu
    private static void dungDuLieu() {

        // Thiên tai và hai khu vực
        baoSo5 = new ThienTai("TT01", "Bão số 5", LoaiThienTai.BAO,
                LocalDateTime.of(2026, 10, 12, 6, 0), "Bão đổ bộ vào miền Trung, mưa lớn kéo dài");
        KhuVuc thanhBinh = new KhuVuc("KV01", "Đà Nẵng", "Hải Châu", "Thanh Bình");
        KhuVuc camChau = new KhuVuc("KV02", "Quảng Nam", "Hội An", "Cẩm Châu");

        batBuoc(quanLy.themThienTai(baoSo5), "thêm thiên tai TT01");
        batBuoc(quanLy.themKhuVuc(thanhBinh), "thêm khu vực KV01");
        batBuoc(quanLy.themKhuVuc(camChau), "thêm khu vực KV02");
        batBuoc(baoSo5.themKhuVuc(thanhBinh), "gắn KV01 vào TT01");
        batBuoc(baoSo5.themKhuVuc(camChau), "gắn KV02 vào TT01");

        // Người dân
        NguoiDan lan = new NguoiDan("079000000001", "Nguyễn Thị Hoài Lan", GioiTinh.NU,
                LocalDate.of(1985, 3, 15), TinhTrangHonNhan.DA_KET_HON, QuanHeChuHo.CHU_HO);
        NguoiDan ngan = new NguoiDan("079000000002", "Trần Thị Kim Ngân", GioiTinh.NU,
                LocalDate.of(1990, 7, 20), TinhTrangHonNhan.DOC_THAN, QuanHeChuHo.CHU_HO);
        NguoiDan hung = new NguoiDan("079000000003", "Phạm Văn Hùng", GioiTinh.NAM,
                LocalDate.of(1978, 1, 10), TinhTrangHonNhan.DA_KET_HON, QuanHeChuHo.CHU_HO);
        NguoiDan binh = new NguoiDan("079000000004", "Lê Thị Bình", GioiTinh.NU,
                LocalDate.of(1975, 9, 5), TinhTrangHonNhan.GOA, QuanHeChuHo.CHU_HO);
        NguoiDan cuong = new NguoiDan("079000000005", "Lê Văn Cường", GioiTinh.NAM,
                LocalDate.of(2000, 12, 1), TinhTrangHonNhan.DOC_THAN, QuanHeChuHo.CON);
        NguoiDan dung = new NguoiDan("079000000006", "Võ Văn Dũng", GioiTinh.NAM,
                LocalDate.of(1970, 4, 22), TinhTrangHonNhan.DA_KET_HON, QuanHeChuHo.CHU_HO);
        NguoiDan em = new NguoiDan("079000000007", "Võ Thị Em", GioiTinh.NU,
                LocalDate.of(1972, 6, 18), TinhTrangHonNhan.DA_KET_HON, QuanHeChuHo.VO_CHONG);
        NguoiDan phuc = new NguoiDan("079000000008", "Đặng Văn Phúc", GioiTinh.NAM,
                LocalDate.of(1988, 11, 30), TinhTrangHonNhan.DOC_THAN, QuanHeChuHo.CHU_HO);

        // Hộ dân
        HoDan hoLan = new HoDan("HD01", "12 Nguyễn Văn Linh", lan);
        HoDan hoNgan = new HoDan("HD02", "25 Lê Duẩn", ngan);
        HoDan hoHung = new HoDan("HD03", "7 Trần Phú", hung);
        HoDan hoBinh = new HoDan("HD04", "40 Hoàng Diệu", binh);
        HoDan hoDung = new HoDan("HD05", "88 Bạch Đằng", dung);
        HoDan hoPhuc = new HoDan("HD06", "15 Nguyễn Duy Hiệu", phuc);

        // Thành viên phải vào hộ trước khi tạo bản ghi
        batBuoc(hoBinh.themThanhVien(cuong), "thêm Lê Văn Cường vào HD04");
        batBuoc(hoDung.themThanhVien(em), "thêm Võ Thị Em vào HD05");

        batBuoc(quanLy.themHoDan("KV01", hoLan), "thêm hộ HD01");
        batBuoc(quanLy.themHoDan("KV01", hoNgan), "thêm hộ HD02");
        batBuoc(quanLy.themHoDan("KV01", hoHung), "thêm hộ HD03");
        batBuoc(quanLy.themHoDan("KV01", hoBinh), "thêm hộ HD04");
        batBuoc(quanLy.themHoDan("KV01", hoDung), "thêm hộ HD05");
        batBuoc(quanLy.themHoDan("KV02", hoPhuc), "thêm hộ HD06");

        // HD01: Lan bị thương, nhà hư hỏng 180 triệu
        BanGhiThietHaiHoDan bgLan = new BanGhiThietHaiHoDan("BG01", THOI_GIAN_GHI, hoLan);
        batBuoc(bgLan.capNhatTrangThaiNguoi(lan.getCCCD(), TrangThaiNguoi.BI_THUONG), "trạng thái Lan");
        batBuoc(bgLan.capNhatThietHaiTaiSan(nhaCua(0, 1, 180_000_000L)), "nhà cửa HD01");
        batBuoc(baoSo5.themBanGhi(bgLan), "thêm BG01");

        // HD02: mất hoa màu và vật nuôi 30 triệu
        BanGhiThietHaiHoDan bgNgan = new BanGhiThietHaiHoDan("BG02", THOI_GIAN_GHI, hoNgan);
        batBuoc(bgNgan.capNhatThietHaiTaiSan(sanXuat(0.5, 20, 30_000_000L)), "sản xuất HD02");
        batBuoc(baoSo5.themBanGhi(bgNgan), "thêm BG02");

        // HD03: nhà hư hỏng nhẹ 3 triệu
        BanGhiThietHaiHoDan bgHung = new BanGhiThietHaiHoDan("BG03", THOI_GIAN_GHI, hoHung);
        batBuoc(bgHung.capNhatThietHaiTaiSan(nhaCua(0, 1, 3_000_000L)), "nhà cửa HD03");
        batBuoc(baoSo5.themBanGhi(bgHung), "thêm BG03");

        // HD04: hai mẹ con bị thương, nhà hư hỏng 150 triệu
        BanGhiThietHaiHoDan bgBinh = new BanGhiThietHaiHoDan("BG04", THOI_GIAN_GHI, hoBinh);
        batBuoc(bgBinh.capNhatTrangThaiNguoi(binh.getCCCD(), TrangThaiNguoi.BI_THUONG), "trạng thái Bình");
        batBuoc(bgBinh.capNhatTrangThaiNguoi(cuong.getCCCD(), TrangThaiNguoi.BI_THUONG), "trạng thái Cường");
        batBuoc(bgBinh.capNhatThietHaiTaiSan(nhaCua(0, 2, 150_000_000L)), "nhà cửa HD04");
        batBuoc(baoSo5.themBanGhi(bgBinh), "thêm BG04");

        // HD05: một người tử vong, một người mất tích, nhà sập 400 triệu, mất hoa màu 45 triệu
        BanGhiThietHaiHoDan bgDung = new BanGhiThietHaiHoDan("BG05", THOI_GIAN_GHI, hoDung);
        batBuoc(bgDung.capNhatTrangThaiNguoi(dung.getCCCD(), TrangThaiNguoi.TU_VONG), "trạng thái Dũng");
        batBuoc(bgDung.capNhatTrangThaiNguoi(em.getCCCD(), TrangThaiNguoi.MAT_TICH), "trạng thái Em");
        batBuoc(bgDung.capNhatThietHaiTaiSan(nhaCua(1, 0, 400_000_000L)), "nhà cửa HD05");
        batBuoc(bgDung.capNhatThietHaiTaiSan(sanXuat(1.2, 35, 45_000_000L)), "sản xuất HD05");
        batBuoc(baoSo5.themBanGhi(bgDung), "thêm BG05");

        // HD06 (KV02): mất hoa màu 20 triệu
        BanGhiThietHaiHoDan bgPhuc = new BanGhiThietHaiHoDan("BG06", THOI_GIAN_GHI, hoPhuc);
        batBuoc(bgPhuc.capNhatThietHaiTaiSan(sanXuat(0.3, 5, 20_000_000L)), "sản xuất HD06");
        batBuoc(baoSo5.themBanGhi(bgPhuc), "thêm BG06");

        // Thiệt hại cơ sở vật chất nhà nước (mỗi đối tượng là một cơ sở cụ thể)
        ThietHaiCauDuong cau1 = new ThietHaiCauDuong("CSVC01", "TT01");
        cau1.capNhatCauDuong(1, 1, 1_500_000_000L);
        batBuoc(quanLy.themCoSoVatChat("KV01", cau1), "thêm CSVC01");

        ThietHaiCongTrinhCong ct1 = new ThietHaiCongTrinhCong("CSVC02", "TT01");
        ct1.capNhatCongTrinh(2, 0, 450_000_000L);
        batBuoc(quanLy.themCoSoVatChat("KV01", ct1), "thêm CSVC02");

        ThietHaiCauDuong cau2 = new ThietHaiCauDuong("CSVC03", "TT01");
        cau2.capNhatCauDuong(2, 0, 300_000_000L);
        batBuoc(quanLy.themCoSoVatChat("KV02", cau2), "thêm CSVC03");

        ThietHaiCongTrinhCong ct2 = new ThietHaiCongTrinhCong("CSVC04", "TT01");
        ct2.capNhatCongTrinh(1, 1, 220_000_000L);
        batBuoc(quanLy.themCoSoVatChat("KV02", ct2), "thêm CSVC04");

        // Kết thúc thiên tai
        batBuoc(baoSo5.ketThucSuKien(LocalDateTime.of(2026, 10, 14, 18, 0)), "kết thúc TT01");
    }

    private static ThietHaiNhaCua nhaCua(int soSap, int soHu, long giaTri) {
        ThietHaiNhaCua t = new ThietHaiNhaCua();
        t.capNhatVeCua(soSap, soHu, giaTri);
        return t;
    }

    private static ThietHaiSanXuat sanXuat(double dienTich, int soVatNuoi, long giaTri) {
        ThietHaiSanXuat t = new ThietHaiSanXuat();
        t.capNhatSanXuat(dienTich, soVatNuoi, giaTri);
        return t;
    }

    //IN BÁO CÁO 

    private static void inBaoCao(ThienTai tt) {

        TreeMap<String, KhuVuc> dsKV = new TreeMap<>(tt.getDsKhuVuc());
        ThongKeThietHai tkToan = tt.thongKeToanBo();        
        dong("                    BÁO CÁO THIỆT HẠI DO THIÊN TAI");
        // 1. Thông tin thiên tai 
        dong("");
        dong("1. THÔNG TIN THIÊN TAI");
        
        dong("Mã thiên tai          : " + tt.getMaTT());
        dong("Tên thiên tai         : " + tt.getTen());
        dong("Loại thiên tai        : " + tenLoaiThienTai(tt.getLoaiThienTai()));
        dong("Trạng thái            : " + tenTrangThaiThienTai(tt.getTrangThai()));
        dong("Thời gian bắt đầu     : " + tt.getThoiGianBatDau().format(GIO));
        dong("Thời gian kết thúc    : "
                + (tt.getThoiGianKetThuc() == null ? "chưa kết thúc" : tt.getThoiGianKetThuc().format(GIO)));
        dong("Mô tả                 : " + tt.getMoTa());
        dong("Số khu vực ảnh hưởng  : " + dsKV.size());
        dong("Số bản ghi hộ dân     : " + tt.getDsBanGhi().size());
        dong("Số khoản thiệt hại cơ sở vật chất nhà nước: " + demCoSoVatChat(tt, null));

        //2. Tổng hợp toàn bộ thiên tai
        dong("");
        dong("2. TỔNG HỢP THIỆT HẠI TOÀN THIÊN TAI");
        inThongKe(tkToan, demNguoiBinhThuong(tt, null), "   ");

        dong("");
        dong("   Bảng tổng hợp theo khu vực:");
        dong(String.format("   %-6s %-14s %5s %6s %8s %9s %16s %16s %17s  %s", "Mã KV", "Xã/Phường", "Số hộ",
                "Chết", "Mất tích", "Bị thương", "Tài sản dân", "CSVC nhà nước", "Tổng vật chất", "Mức độ"));
        for (KhuVuc kv : dsKV.values()) {
            ThongKeThietHai tk = tt.thongKeKhuVuc(kv.getMaKV());
            dong(String.format("   %-6s %-14s %5d %6d %8d %9d %16s %16s %17s  %s", kv.getMaKV(), kv.getXaPhuong(),
                    kv.demSoHoDan(), tk.getSoNguoiChet(), tk.getSoNguoiMatTich(), tk.getSoNguoiBiThuong(),
                    so(tk.tongGiaTriTaiSanDan()), so(tk.tongGiaTriCoSoVatChatNN()), so(tk.tongGiaTriVatChat()),
                    tenMucDo(tt.danhGiaKhuVuc(kv.getMaKV()))));
        }

        //3. Chi tiết từng khu vực 
        dong("");
        dong("3. CHI TIẾT TỪNG KHU VỰC");
        int stt = 0;
        for (KhuVuc kv : dsKV.values()) {
            stt++;
            inChiTietKhuVuc(tt, kv, stt);
        }

        //4. Danh sách hộ dân theo mức độ
        dong("");
        dong("4. DANH SÁCH HỘ DÂN THEO MỨC ĐỘ THIỆT HẠI (TOÀN THIÊN TAI)");
        
        for (Map.Entry<MucDoThietHai, List<HoDan>> e : tt.nhomHoTheoMucDo().entrySet()) {
            dong("Mức " + tenMucDo(e.getKey()) + ": " + e.getValue().size() + " hộ");
            for (HoDan ho : e.getValue()) {
                dong("   - " + ho.getMaHo() + " | chủ hộ " + ho.getChuHo().getHoTen() + " | " + ho.getDiaChi()
                        + " | khu vực " + ho.getKhuVuc().getMaKV() + " (" + ho.getKhuVuc().getXaPhuong() + ")");
            }
        }

        //5. Danh sách khu vực theo mức độ 
        dong("");
        dong("5. DANH SÁCH KHU VỰC THEO MỨC ĐỘ THIỆT HẠI");
        for (MucDoThietHai m : MucDoThietHai.values()) {
            List<KhuVuc> ds = tt.danhSachKhuVucTheoMucDo(m);
            StringBuilder sb = new StringBuilder();
            for (KhuVuc kv : ds) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(kv.getMaKV()).append(" (").append(kv.getXaPhuong()).append(")");
            }
            dong("Mức " + tenMucDo(m) + ": " + ds.size() + " khu vực" + (ds.isEmpty() ? "" : " -> " + sb));
        }
    }

    // In chi tiết một khu vực: thông tin chung, cơ sở vật chất nhà nước, từng hộ dân
    private static void inChiTietKhuVuc(ThienTai tt, KhuVuc kv, int stt) {

        BaoCao bc = new BaoCao("BC" + String.format("%02d", stt),
                "Báo cáo thiệt hại " + tt.getTen() + " tại " + kv.getXaPhuong(), tt, kv);
        ThongKeThietHai tk = tt.thongKeKhuVuc(kv.getMaKV());

        dong("");      
        dong("3." + stt + ". KHU VỰC " + kv.getMaKV() + " - " + kv.getXaPhuong());
        dong("Mã báo cáo            : " + bc.getMaBaoCao());
        dong("Tên báo cáo           : " + bc.getTenBaoCao());
        dong("Thời gian lập         : " + bc.getThoiGianLap().format(GIO));
        dong("Địa bàn               : " + kv.getXaPhuong() + ", " + kv.getQuanHuyen() + ", " + kv.getTinhThanhPho());
        dong("Tổng số hộ dân        : " + kv.demSoHoDan());
        dong("Mức độ thiệt hại khu vực: " + tenMucDo(tt.danhGiaKhuVuc(kv.getMaKV())));

        // Tổng hợp khu vực
        dong("");
        dong("A. Tổng hợp thiệt hại khu vực");
        inThongKe(tk, demNguoiBinhThuong(tt, kv), "   ");

        // Cơ sở vật chất nhà nước
        dong("");
        dong("B. Thiệt hại cơ sở vật chất nhà nước (" + demCoSoVatChat(tt, kv) + " khoản)");
        TreeMap<String, ThietHaiCoSoVatChat_NN> dsCSVC = new TreeMap<>(kv.getDsThietHaiCoSoVatChatNN());
        int soKhoan = 0;
        for (ThietHaiCoSoVatChat_NN t : dsCSVC.values()) {
            if (!tt.getMaTT().equals(t.getMaTT())) continue;
            soKhoan++;
            inCoSoVatChat(t, soKhoan);
        }
        if (soKhoan == 0) dong("   (không có khoản thiệt hại cơ sở vật chất nào)");

        // Từng hộ dân
        dong("");
        dong("C. Chi tiết từng hộ dân");
        int soHoCoBanGhi = 0;
        List<HoDan> hoChuaGhi = new ArrayList<>(new TreeMap<>(kv.getDsHoDan()).values());
        for (BanGhiThietHaiHoDan bg : tt.getDsBanGhi().values()) {
            if (bg.getHoDan().getKhuVuc() != kv) continue;
            soHoCoBanGhi++;
            hoChuaGhi.remove(bg.getHoDan());
            inHoDan(bg, soHoCoBanGhi);
        }
        if (soHoCoBanGhi == 0) dong("   (khu vực chưa có bản ghi thiệt hại hộ dân)");

        dong("");
        if (hoChuaGhi.isEmpty()) {
            dong("   Hộ chưa có bản ghi thiệt hại: không có");
        } else {
            for (HoDan ho : hoChuaGhi) {
                dong("   Hộ chưa có bản ghi thiệt hại: " + ho.getMaHo() + " - chủ hộ " + ho.getChuHo().getHoTen());
            }
        }
    }

    // In một khoản thiệt hại cơ sở vật chất nhà nước (có mã và loại CSVC)
    private static void inCoSoVatChat(ThietHaiCoSoVatChat_NN t, int stt) {

        dong("   [" + stt + "] Mã CSVC: " + t.getMaCSVC());
        dong("       Loại CSVC            : " + t.getLoaiCSVC() + " (" + tenLoaiCSVC(t.getLoaiCSVC()) + ")");
        dong("       Thuộc thiên tai      : " + t.getMaTT());

        if (t instanceof ThietHaiCauDuong) {
            ThietHaiCauDuong c = (ThietHaiCauDuong) t;
            dong("       Số cầu/đường hư hỏng : " + c.getSoCauBiHuHong());
            dong("       Số cầu/đường bị sập  : " + c.getSoCauBiSap());
            dong("       Tổng số bị ảnh hưởng : " + (c.getSoCauBiHuHong() + c.getSoCauBiSap()));
        } else if (t instanceof ThietHaiCongTrinhCong) {
            ThietHaiCongTrinhCong c = (ThietHaiCongTrinhCong) t;
            dong("       Số công trình hư hỏng: " + c.getSoCongTrinhBiHuHong());
            dong("       Số công trình sập đổ : " + c.getSoCongTrinhBiSapDo());
            dong("       Tổng số bị ảnh hưởng : " + (c.getSoCongTrinhBiHuHong() + c.getSoCongTrinhBiSapDo()));
        }

        dong("       Giá trị thiệt hại    : " + tien(t.tinhTongThietHai()));
    }

    // In một hộ dân: thông tin hộ, từng thành viên, thiệt hại về người, thiệt hại tài sản
    private static void inHoDan(BanGhiThietHaiHoDan bg, int stt) {

        HoDan ho = bg.getHoDan();
        ThongKeThietHai tk = bg.tinhThongKe();
        Map<String, TrangThaiNguoi> trangThai = bg.getDsTrangThaiNguoi();

        dong("");
        dong("   Hộ " + stt + ": " + ho.getMaHo() + " - " + ho.getDiaChi());
        dong("   Mã bản ghi          : " + bg.getMaBanGhi());
        dong("   Thời gian ghi nhận  : " + bg.getThoiGianGhiNhan().format(GIO));
        dong("   Chủ hộ              : " + ho.getChuHo().getHoTen() + " (CCCD " + ho.getChuHo().getCCCD() + ")");
        dong("   Số thành viên       : " + ho.demSoThanhVien());
        dong("   Mức độ thiệt hại hộ : " + tenMucDo(bg.danhGiaMucDo()));

        // Thành viên
        dong("");
        dong("   a) Thành viên và tình trạng");
        dong(String.format("      %-3s %-13s %-22s %-9s %-11s %-10s %-11s %-11s %s", "STT", "CCCD", "Họ tên",
                "Giới tính", "Ngày sinh", "Hôn nhân", "Quan hệ", "Trạng thái", "Còn sống"));
        int i = 0;
        int soBinhThuong = 0;
        for (NguoiDan nd : ho.getDsThanhVien()) {
            i++;
            TrangThaiNguoi tt = trangThai.get(nd.getCCCD());
            if (tt == TrangThaiNguoi.BINH_THUONG) soBinhThuong++;
            dong(String.format("      %-3d %-13s %-22s %-9s %-11s %-10s %-11s %-11s %s", i, nd.getCCCD(),
                    nd.getHoTen(), tenGioiTinh(nd.getGioiTinh()), nd.getNgaySinh().format(NGAY),
                    tenHonNhan(nd.getTinhTrangHonNhan()), tenQuanHe(nd.getQuanHeChuHo()), tenTrangThaiNguoi(tt),
                    nd.isConSong() ? "Có" : "Không"));
        }

        dong("");
        dong("   b) Thiệt hại về người: chết " + tk.getSoNguoiChet() + " | mất tích " + tk.getSoNguoiMatTich()
                + " | bị thương " + tk.getSoNguoiBiThuong() + " | bình thường " + soBinhThuong
                + " | tổng bị ảnh hưởng " + tk.tongSoNguoiBiAnhHuong());

        // Tài sản
        dong("");
        dong("   c) Thiệt hại về của (tài sản người dân)");
        boolean coNha = false;
        boolean coSanXuat = false;
        for (ThietHaiTaiSanDan t : bg.getDsThietHaiTaiSan()) {
            if (t instanceof ThietHaiNhaCua) {
                ThietHaiNhaCua n = (ThietHaiNhaCua) t;
                coNha = true;
                dong("      - Nhà cửa : sập hoàn toàn " + n.getSoNhaSapHoanToan() + " | hư hỏng " + n.getSoNhaHuHong()
                        + " | giá trị " + tien(n.tinhTongThietHai()));
            } else if (t instanceof ThietHaiSanXuat) {
                ThietHaiSanXuat s = (ThietHaiSanXuat) t;
                coSanXuat = true;
                dong("      - Sản xuất: diện tích cây trồng thiệt hại "
                        + String.format(Locale.US, "%.2f", s.getDienTichCayTrongBiThietHai())
                        + " | vật nuôi thiệt hại " + s.getSoVatNuoiBiThietHai() + " | giá trị "
                        + tien(s.tinhTongThietHai()));
            }
        }
        if (!coNha) dong("      - Nhà cửa : không có thiệt hại");
        if (!coSanXuat) dong("      - Sản xuất: không có thiệt hại");
        dong("      Tổng thiệt hại tài sản của hộ: " + tien(tk.tongGiaTriTaiSanDan()));
    }

    // In khối số liệu thống kê (người và của)
    private static void inThongKe(ThongKeThietHai tk, int soBinhThuong, String le) {
        dong(le + "Thiệt hại về người");
        dong(le + "   Số người chết          : " + tk.getSoNguoiChet());
        dong(le + "   Số người mất tích      : " + tk.getSoNguoiMatTich());
        dong(le + "   Số người bị thương     : " + tk.getSoNguoiBiThuong());
        dong(le + "   Số người bình thường   : " + soBinhThuong);
        dong(le + "   Tổng số bị ảnh hưởng   : " + tk.tongSoNguoiBiAnhHuong());
        dong(le + "Thiệt hại về của");
        dong(le + "   Nhà cửa                : " + tien(tk.getTongGiaTriNhaCua()));
        dong(le + "   Sản xuất               : " + tien(tk.getTongGiaTriSanXuat()));
        dong(le + "   => Tài sản người dân   : " + tien(tk.tongGiaTriTaiSanDan()));
        dong(le + "   Cầu đường              : " + tien(tk.getTongGiaTriCauDuong()));
        dong(le + "   Công trình công        : " + tien(tk.getTongGiaTriCongTrinhCong()));
        dong(le + "   => CSVC nhà nước       : " + tien(tk.tongGiaTriCoSoVatChatNN()));
        dong(le + "TỔNG THIỆT HẠI VẬT CHẤT   : " + tien(tk.tongGiaTriVatChat()));
    }
    //hàm hỗ trợ
    // Đếm số người ở trạng thái bình thường trong các bản ghi (kv = null là toàn thiên tai)
    private static int demNguoiBinhThuong(ThienTai tt, KhuVuc kv) {
        int dem = 0;
        for (BanGhiThietHaiHoDan bg : tt.getDsBanGhi().values()) {
            if (kv != null && bg.getHoDan().getKhuVuc() != kv) continue;
            for (TrangThaiNguoi t : bg.getDsTrangThaiNguoi().values()) {
                if (t == TrangThaiNguoi.BINH_THUONG) dem++;
            }
        }
        return dem;
    }

    // Đếm số khoản thiệt hại cơ sở vật chất của thiên tai (kv = null là mọi khu vực)
    private static int demCoSoVatChat(ThienTai tt, KhuVuc kv) {
        int dem = 0;
        for (KhuVuc k : tt.getDsKhuVuc().values()) {
            if (kv != null && k != kv) continue;
            for (ThietHaiCoSoVatChat_NN t : k.getDsThietHaiCoSoVatChatNN().values()) {
                if (tt.getMaTT().equals(t.getMaTT())) dem++;
            }
        }
        return dem;
    }

    // Dừng chương trình nếu một bước dựng dữ liệu thất bại
    private static void batBuoc(boolean thanhCong, String buoc) {
        if (!thanhCong) {
            throw new IllegalStateException("Bước dựng dữ liệu thất bại: " + buoc);
        }
    }

    private static void dong(String s) {
        System.out.println(s);
    }

   

    // Số có dấu phẩy ngăn cách hàng nghìn
    private static String so(long v) {
        return String.format(Locale.US, "%,d", v);
    }

    private static String tien(long v) {
        return so(v) + " VNĐ";
    }

    private static String tenMucDo(MucDoThietHai m) {
        switch (m) {
            case KHONG_DANG_KE: return "Không đáng kể";
            case NHE:           return "Nhẹ";
            case TRUNG_BINH:    return "Trung bình";
            case NANG:          return "Nặng";
            case RAT_NANG:      return "Rất nặng";
            default:            return m.name();
        }
    }

    private static String tenLoaiThienTai(LoaiThienTai l) {
        switch (l) {
            case BAO:        return "Bão";
            case LU_LUT:     return "Lũ lụt";
            case DONG_DAT:   return "Động đất";
            case HAN_HAN:    return "Hạn hán";
            case SAT_LO_DAT: return "Sạt lở đất";
            case CHAY_RUNG:  return "Cháy rừng";
            default:         return "Khác";
        }
    }

    private static String tenTrangThaiThienTai(TrangThaiThienTai t) {
        return (t == TrangThaiThienTai.DANG_DIEN_RA) ? "Đang diễn ra" : "Đã kết thúc";
    }

    private static String tenLoaiCSVC(String loai) {
        if ("CAU_DUONG".equals(loai)) return "Cầu đường";
        if ("CONG_TRINH_CONG".equals(loai)) return "Công trình công";
        return loai;
    }

    private static String tenTrangThaiNguoi(TrangThaiNguoi t) {
        if (t == null) return "Chưa ghi";
        switch (t) {
            case BINH_THUONG: return "Bình thường";
            case BI_THUONG:   return "Bị thương";
            case MAT_TICH:    return "Mất tích";
            case TU_VONG:     return "Tử vong";
            default:          return t.name();
        }
    }

    private static String tenGioiTinh(GioiTinh g) {
        switch (g) {
            case NAM: return "Nam";
            case NU:  return "Nữ";
            default:  return "Khác";
        }
    }

    private static String tenHonNhan(TinhTrangHonNhan h) {
        switch (h) {
            case DOC_THAN:   return "Độc thân";
            case DA_KET_HON: return "Đã kết hôn";
            case LY_HON:     return "Ly hôn";
            case GOA:        return "Góa";
            default:         return h.name();
        }
    }

    private static String tenQuanHe(QuanHeChuHo q) {
        switch (q) {
            case CHU_HO:     return "Chủ hộ";
            case VO_CHONG:   return "Vợ/chồng";
            case CON:        return "Con";
            case CHA_ME:     return "Cha/mẹ";
            case ANH_CHI_EM: return "Anh/chị/em";
            default:         return "Khác";
        }
    }

    //BỔ SUNG test: kinh phí các thứ, test các chức năng luôn

    //6. Báo cáo kinh phí khắc phục

    // Dựng các nguồn kinh phí mẫu cho thiên tai (mỗi nguồn là một bên, góp nhiều đợt thì cộng dồn)
    private static void dungDuLieuKinhPhi(ThienTai tt) {

        KhacPhucThienTai kp = tt.getKhacPhuc();
        String ma = tt.getMaTT();

        // Nhà nước
        NguonNhaNuoc tw = new NguonNhaNuoc("KP01", "Ngân sách Trung ương", ma, CapNganSach.TRUNG_UONG);
        batBuoc(tw.themDongGop(1_500_000_000L), "KP01 đợt 1");
        batBuoc(tw.themDongGop(500_000_000L), "KP01 đợt 2");
        NguonNhaNuoc tinh = new NguonNhaNuoc("KP02", "Ngân sách thành phố Đà Nẵng", ma, CapNganSach.TINH);
        batBuoc(tinh.themDongGop(500_000_000L), "KP02 đợt 1");

        // Cá nhân
        NguonCaNhan an = new NguonCaNhan("KP03", "Nguyễn Văn An", ma, "079300000001");
        batBuoc(an.themDongGop(500_000L), "KP03 đợt 1");
        batBuoc(an.themDongGop(400_000L), "KP03 đợt 2");
        batBuoc(an.themDongGop(200_000L), "KP03 đợt 3");
        NguonCaNhan binh = new NguonCaNhan("KP04", "Trần Thị Bình", ma, "079300000002");
        batBuoc(binh.themDongGop(900_000L), "KP04 đợt 1");
        NguonCaNhan cuong = new NguonCaNhan("KP05", "Lê Văn Cường", ma, "079300000003");
        batBuoc(cuong.themDongGop(3_000_000L), "KP05 đợt 1");
        batBuoc(cuong.themDongGop(9_000_000L), "KP05 đợt 2");
        NguonCaNhan dung = new NguonCaNhan("KP06", "Phạm Thị Dung", ma, "079300000004");
        batBuoc(dung.themDongGop(25_000_000L), "KP06 đợt 1");

        // Tập thể
        NguonTapThe chuThapDo = new NguonTapThe("KP07", "Hội Chữ thập đỏ phường Thanh Bình", ma, "Nguyễn Văn Hòa");
        batBuoc(chuThapDo.themDongGop(6_000_000L), "KP07 đợt 1");
        batBuoc(chuThapDo.themDongGop(2_000_000L), "KP07 đợt 2");
        NguonTapThe doan = new NguonTapThe("KP08", "Đoàn Thanh niên quận Hải Châu", ma, "Trần Minh Khoa");
        batBuoc(doan.themDongGop(40_000_000L), "KP08 đợt 1");
        NguonTapThe hoiDN = new NguonTapThe("KP09", "Hội Doanh nghiệp trẻ Đà Nẵng", ma, "Lê Thu Hà");
        batBuoc(hoiDN.themDongGop(80_000_000L), "KP09 đợt 1");
        batBuoc(hoiDN.themDongGop(40_000_000L), "KP09 đợt 2");

        // Công ty
        NguonCongTy abc = new NguonCongTy("KP10", "Công ty TNHH ABC", ma, "0101234567");
        batBuoc(abc.themDongGop(400_000_000L), "KP10 đợt 1");
        batBuoc(abc.themDongGop(200_000_000L), "KP10 đợt 2");
        NguonCongTy xyz = new NguonCongTy("KP11", "Tập đoàn XYZ", ma, "0309876543-001");
        batBuoc(xyz.themDongGop(3_000_000_000L), "KP11 đợt 1");
        NguonCongTy minhPhat = new NguonCongTy("KP12", "Công ty CP Minh Phát", ma, "0200123456");
        batBuoc(minhPhat.themDongGop(30_000_000L), "KP12 đợt 1");

        NguonKinhPhi[] tatCa = { tw, tinh, an, binh, cuong, dung, chuThapDo, doan, hoiDN, abc, xyz, minhPhat };
        for (NguonKinhPhi n : tatCa) {
            batBuoc(kp.themNguon(n), "thêm nguồn " + n.getMaNguon());
        }
    }

    // Báo cáo kinh phí: chi tiết từng nguồn, tổng hợp, mức đóng góp, so với thiệt hại
    private static void inBaoCaoKinhPhi(ThienTai tt) {

        dungDuLieuKinhPhi(tt);

        KhacPhucThienTai kp = tt.getKhacPhuc();
        ThongKeDongGop tk = kp.thongKeDongGop();
        TreeMap<String, NguonKinhPhi> dsNguon = kp.getDsNguonKinhPhi();

        dong("");
        dong("");
        dong("                    BÁO CÁO KINH PHÍ KHẮC PHỤC THIÊN TAI");

        // 6.1 Tổng quan
        int tongLan = 0;
        for (NguonKinhPhi n : dsNguon.values()) {
            tongLan += n.getSoLanDongGop();
        }
        dong("");
        dong("6. TỔNG QUAN KINH PHÍ");
        dong("Thiên tai                         : " + tt.getMaTT() + " - " + tt.getTen());
        dong("Số nguồn kinh phí                 : " + kp.demSoNguon());
        dong("Tổng số lần đóng góp (cộng từ từng nguồn): " + tongLan);
        dong("Tổng số lần đóng góp (theo ThongKeDongGop): " + tk.demSoLanDongGop());
        dong("Tổng kinh phí huy động            : " + tien(kp.tinhTongKinhPhi()));
        dong("Tổng kinh phí (theo ThongKeDongGop): " + tien(tk.tongDongGop()));

        // 6.2 Chi tiết từng nguồn theo nhóm
        dong("");
        dong("6.1. CHI TIẾT TỪNG NGUỒN KINH PHÍ");
        for (NhomNguon nhom : NhomNguon.values()) {
            int stt = 0;
            for (NguonKinhPhi n : dsNguon.values()) {
                if (n.getNhomNguon() == nhom) stt++;
            }
            dong("");
            dong("   Nhóm " + tenNhomNguon(nhom) + " (" + stt + " nguồn)");
            stt = 0;
            for (NguonKinhPhi n : dsNguon.values()) {
                if (n.getNhomNguon() != nhom) continue;
                stt++;
                inNguonKinhPhi(n, stt, tk);
            }
        }

        // 6.3 Tổng hợp theo nhóm
        dong("");
        dong("6.2. TỔNG HỢP THEO NHÓM NGUỒN");
        dong(String.format("   %-12s %8s %20s %10s", "Nhóm", "Số bên", "Tổng tiền", "Tỷ lệ"));
        for (NhomNguon nhom : NhomNguon.values()) {
            dong(String.format("   %-12s %8d %20s %9.2f%%", tenNhomNguon(nhom), tk.demSoBenTheoNhom().get(nhom),
                    so(tk.tongTheoNhom().get(nhom)), tk.tyLeTheoNhom().get(nhom)));
        }
        dong(String.format("   %-12s %8d %20s %9.2f%%", "Cộng", tk.demSoBen(), so(tk.tongDongGop()), 100.0));

        // 6.4 Kinh phí Nhà nước theo cấp ngân sách
        dong("");
        dong("6.3. KINH PHÍ NHÀ NƯỚC THEO CẤP NGÂN SÁCH");
        long tongNhaNuoc = tk.tongTheoNhom().get(NhomNguon.NHA_NUOC);
        for (Map.Entry<CapNganSach, Long> e : kp.tongNhaNuocTheoCapNganSach().entrySet()) {
            double tyLe = tongNhaNuoc == 0 ? 0.0 : e.getValue() * 100.0 / tongNhaNuoc;
            dong("   " + tenCapNganSach(e.getKey()) + ": " + tien(e.getValue()) + " (" + String.format(Locale.US, "%.2f", tyLe)
                    + "% kinh phí Nhà nước)");
        }
        dong("   Tổng kinh phí Nhà nước: " + tien(tongNhaNuoc));

        // 6.5 Các bên đóng góp (không gồm Nhà nước)
        dong("");
        dong("6.4. CÁC BÊN ĐÓNG GÓP (KHÔNG GỒM NHÀ NƯỚC)");
        dong("   Số bên đóng góp        : " + (tk.demSoBen() - tk.demSoBenTheoNhom().get(NhomNguon.NHA_NUOC)));
        dong("   Tổng tiền đóng góp     : " + tien(tk.tongDongGop() - tongNhaNuoc));
        dong("   Trung bình mỗi bên     : " + tien(Math.round(tk.trungBinhMoiBen())));
        inBenNoiBat("   Bên đóng góp cao nhất  : ", tk.benCaoNhat(), kp, tk);
        inBenNoiBat("   Bên đóng góp thấp nhất : ", tk.benThapNhat(), kp, tk);

        // 6.6 Đánh giá mức độ đóng góp
        dong("");
        dong("6.5. ĐÁNH GIÁ MỨC ĐỘ ĐÓNG GÓP");
        for (MucDoDongGop m : MucDoDongGop.values()) {
            List<String> ds = tk.danhSachBenTheoMucDo(m);
            dong("   Mức " + tenMucDongGop(m) + ": " + ds.size() + " bên");
            for (String maBen : ds) {
                NguonKinhPhi n = kp.timNguon(maBen);
                dong("      - " + maBen + " | " + n.getTenNguon() + " | nhóm " + tenNhomNguon(n.getNhomNguon()) + " | "
                        + tien(n.getTongSoTien()));
            }
        }

        // 6.7 So sánh với thiệt hại
        long thietHai = tt.tinhTongThietHaiVatChat();
        long kinhPhi = kp.tinhTongKinhPhi();
        dong("");
        dong("6.6. SO SÁNH KINH PHÍ VỚI THIỆT HẠI");
        dong("   Tổng thiệt hại vật chất : " + tien(thietHai));
        dong("   Tổng kinh phí huy động  : " + tien(kinhPhi));
        if (thietHai > 0) {
            dong("   Kinh phí / thiệt hại    : " + String.format(Locale.US, "%.2f", kinhPhi * 100.0 / thietHai) + "%");
        }
        dong("   " + (kinhPhi >= thietHai ? "Kinh phí vượt thiệt hại" : "Kinh phí còn thiếu so với thiệt hại") + " : "
                + tien(Math.abs(kinhPhi - thietHai)));
    }

    // In một nguồn kinh phí: thông tin chung, thông tin riêng theo nhóm, mức đóng góp
    private static void inNguonKinhPhi(NguonKinhPhi n, int stt, ThongKeDongGop tk) {

        dong("   [" + stt + "] Mã nguồn       : " + n.getMaNguon());
        dong("       Tên nguồn       : " + n.getTenNguon());
        dong("       Thuộc thiên tai : " + n.getMaTT());

        if (n instanceof NguonCaNhan) {
            dong("       CCCD            : " + ((NguonCaNhan) n).getCCCD());
        } else if (n instanceof NguonCongTy) {
            dong("       Mã số thuế      : " + ((NguonCongTy) n).getMaSoThue());
        } else if (n instanceof NguonTapThe) {
            dong("       Người đại diện  : " + ((NguonTapThe) n).getNguoiDaiDien());
        } else if (n instanceof NguonNhaNuoc) {
            dong("       Cấp ngân sách   : " + tenCapNganSach(((NguonNhaNuoc) n).getCapNganSach()));
        }

        dong("       Số lần đóng góp : " + n.getSoLanDongGop());
        dong("       Tổng số tiền    : " + tien(n.getTongSoTien()));

        if (n.getNhomNguon() == NhomNguon.NHA_NUOC) {
            dong("       Mức đóng góp    : không chấm mức (ngân sách Nhà nước)");
        } else {
            dong("       Mức đóng góp    : " + tenMucDongGop(tk.mucDoDongGop(n.getMaNguon())));
        }
    }

    // In bên nổi bật (cao nhất / thấp nhất) kèm tên, nhóm, số tiền
    private static void inBenNoiBat(String nhan, String maBen, KhacPhucThienTai kp, ThongKeDongGop tk) {
        if (maBen == null) {
            dong(nhan + "(chưa có bên nào)");
            return;
        }
        NguonKinhPhi n = kp.timNguon(maBen);
        dong(nhan + maBen + " - " + n.getTenNguon() + " | nhóm " + tenNhomNguon(n.getNhomNguon()) + " | "
                + tien(n.getTongSoTien()) + " | mức " + tenMucDongGop(tk.mucDoDongGop(maBen)));
    }

    private static String tenNhomNguon(NhomNguon n) {
        switch (n) {
            case NHA_NUOC: return "Nhà nước";
            case CA_NHAN:  return "Cá nhân";
            case TAP_THE:  return "Tập thể";
            case CONG_TY:  return "Công ty";
            default:       return n.name();
        }
    }

    private static String tenCapNganSach(CapNganSach c) {
        switch (c) {
            case TRUNG_UONG: return "Ngân sách Trung ương";
            case TINH:       return "Ngân sách tỉnh";
            default:         return c.name();
        }
    }

    private static String tenMucDongGop(MucDoDongGop m) {
        switch (m) {
            case THAP:       return "Thấp";
            case TRUNG_BINH: return "Trung bình";
            case CAO:        return "Cao";
            case RAT_CAO:    return "Rất cao";
            default:         return m.name();
        }
    }

    // ===================== 7. Báo cáo hỗ trợ và chi khắc phục =====================

    // Bổ sung hai nguồn có mục đích sử dụng chỉ định, các hoạt động khắc phục và các khoản chi mẫu.
    // Mọi khoản chi đều đi qua ghiNhanChi (đã kiểm tra nguồn đủ tiền, đúng mục đích).
    private static void dungDuLieuHoTro(ThienTai tt) {

        KhacPhucThienTai kp = tt.getKhacPhuc();
        String ma = tt.getMaTT();

        // Nguồn có mục đích chỉ định: phải đặt mục đích TRƯỚC khi thêm vào khắc phục
        // (thêm xong thì nhãn bị khóa, không đổi được nữa)
        NguonCaNhan hanh = new NguonCaNhan("KP13", "Nguyễn Thị Hạnh (chỉ giúp hộ dân)", ma, "079300000005");
        batBuoc(hanh.themDongGop(60_000_000L), "KP13 đợt 1");
        hanh.capNhatMucDichSuDung(MucDichSuDung.HO_TRO_NGUOI_DAN);
        NguonCongTy cauDuong = new NguonCongTy("KP14", "Công ty CP Cầu đường Miền Trung (chỉ cho hạ tầng)", ma, "0400222333");
        batBuoc(cauDuong.themDongGop(500_000_000L), "KP14 đợt 1");
        cauDuong.capNhatMucDichSuDung(MucDichSuDung.CO_SO_HA_TANG);
        batBuoc(kp.themNguon(hanh), "thêm nguồn KP13");
        batBuoc(kp.themNguon(cauDuong), "thêm nguồn KP14");

        // Hỗ trợ hộ dân (nhu cầu: HD01 30tr, HD03 25tr, HD04 60tr, HD05 100tr)
        hoatDongHoDan(kp, "HT01", "Hỗ trợ hộ HD05 (có người chết, mất tích, nhà sập)", "KV01", "HD05", 100_000_000L);
        hoatDongHoDan(kp, "HT02", "Hỗ trợ hộ HD04 (hai người bị thương, hai nhà hư hỏng)", "KV01", "HD04", 60_000_000L);
        hoatDongHoDan(kp, "HT03", "Hỗ trợ hộ HD01 (một người bị thương, nhà hư hỏng)", "KV01", "HD01", 30_000_000L);
        hoatDongHoDan(kp, "HT04", "Hỗ trợ bổ sung hộ HD04", "KV01", "HD04", 20_000_000L);
        hoatDongHoDan(kp, "HT05", "Hỗ trợ hộ HD03 (nhà hư hỏng)", "KV01", "HD03", 25_000_000L);

        // Hạ tầng: sửa chữa công trình công, XÂY ĐƯỜNG MỚI thay cầu đường bị sập, sửa cầu ở KV02
        hoatDongHaTang(kp, "HT06", "Sửa chữa công trình công bị hư hỏng", LoaiHoatDong.SUA_CHUA_HA_TANG,
                "KV01", "CSVC02", 150_000_000L);
        hoatDongHaTang(kp, "HT07", "Xây đoạn đường mới thay đoạn cầu đường bị sập", LoaiHoatDong.XAY_MOI_HA_TANG,
                "KV01", "CSVC01", 800_000_000L);
        hoatDongHaTang(kp, "HT08", "Sửa chữa cầu đường bị hư hỏng ở Cẩm Châu", LoaiHoatDong.SUA_CHUA_HA_TANG,
                "KV02", "CSVC03", 40_000_000L);

        // Chi tiền: hộ dân (nguồn Nhà nước, nguồn đóng góp chỉ giúp dân)
        chi(kp, "HT01", "KP01", 40_000_000L);
        chi(kp, "HT01", "KP13", 60_000_000L);       // HT01 chi từ hai nguồn
        chi(kp, "HT02", "KP02", 20_000_000L);
        chi(kp, "HT03", "KP01", 30_000_000L);
        chi(kp, "HT04", "KP01", 15_000_000L);       // hộ HD04 có hai hoạt động: cộng dồn 35 triệu
        // HT05 chưa chi đồng nào (mới lập kế hoạch)

        // Chi tiền: hạ tầng (nguồn đóng góp chỉ cho hạ tầng, nguồn Nhà nước)
        chi(kp, "HT06", "KP14", 160_000_000L);      // chi vượt dự kiến 10 triệu
        chi(kp, "HT07", "KP14", 340_000_000L);
        chi(kp, "HT08", "KP01", 52_000_000L);       // chi vượt dự kiến 12 triệu

        // Tiến độ (HT05 giữ CHUA_BAT_DAU)
        kp.timHoatDong("HT01").capNhatTrangThai(TrangThaiKhacPhuc.HOAN_THANH);
        kp.timHoatDong("HT02").capNhatTrangThai(TrangThaiKhacPhuc.DANG_THUC_HIEN);
        kp.timHoatDong("HT03").capNhatTrangThai(TrangThaiKhacPhuc.HOAN_THANH);
        kp.timHoatDong("HT04").capNhatTrangThai(TrangThaiKhacPhuc.DANG_THUC_HIEN);
        kp.timHoatDong("HT06").capNhatTrangThai(TrangThaiKhacPhuc.HOAN_THANH);
        kp.timHoatDong("HT07").capNhatTrangThai(TrangThaiKhacPhuc.DANG_THUC_HIEN);
        kp.timHoatDong("HT08").capNhatTrangThai(TrangThaiKhacPhuc.HOAN_THANH);
    }

    private static void hoatDongHoDan(KhacPhucThienTai kp, String ma, String ten, String maKV, String maHo, long duKien) {
        batBuoc(kp.themHoatDong(new HoatDongKhacPhuc(ma, ten, LoaiHoatDong.HO_TRO_HO_DAN, maKV, maHo, null, duKien)),
                "thêm hoạt động " + ma);
    }

    private static void hoatDongHaTang(KhacPhucThienTai kp, String ma, String ten, LoaiHoatDong loai, String maKV,
            String maCSVC, long duKien) {
        batBuoc(kp.themHoatDong(new HoatDongKhacPhuc(ma, ten, loai, maKV, null, maCSVC, duKien)), "thêm hoạt động " + ma);
    }

    private static void chi(KhacPhucThienTai kp, String maHoatDong, String maNguon, long soTien) {
        batBuoc(kp.ghiNhanChi(maHoatDong, maNguon, soTien), "chi " + maHoatDong + " từ " + maNguon);
    }

    // Báo cáo hỗ trợ và chi: hoạt động, chi từ nguồn nào, thống kê chi, đánh giá mức đáp ứng
    private static void inBaoCaoHoTro(ThienTai tt) {

        dungDuLieuHoTro(tt);

        KhacPhucThienTai kp = tt.getKhacPhuc();
        ThongKeChi tk = new ThongKeChi(kp);
        DanhGiaHoTro dg = new DanhGiaHoTro(tt);
        TreeMap<String, KhuVuc> dsKV = new TreeMap<>(tt.getDsKhuVuc());
        TreeMap<String, NguonKinhPhi> dsNguon = kp.getDsNguonKinhPhi();
        TreeMap<String, HoatDongKhacPhuc> dsHoatDong = kp.getDsHoatDong();

        dong("");
        dong("");
        dong("                    BÁO CÁO HỖ TRỢ VÀ CHI KHẮC PHỤC THIÊN TAI");

        // 7. Tổng quan
        dong("");
        dong("7. TỔNG QUAN HUY ĐỘNG VÀ CHI");
        dong("Thiên tai                 : " + tt.getMaTT() + " - " + tt.getTen());
        dong("Số nguồn kinh phí         : " + kp.demSoNguon() + " (đã bổ sung KP13, KP14 là hai nguồn chỉ định mục đích)");
        dong("Số hoạt động khắc phục    : " + kp.demSoHoatDong());
        dong("Tổng kinh phí huy động    : " + tien(tk.tongHuyDong()));
        dong("Tổng đã chi               : " + tien(tk.tongChi()));
        dong("Còn lại                   : " + tien(tk.conLai()));
        dong("Tỷ lệ đã chi              : " + String.format(Locale.US, "%.2f", tk.tyLeDaChi()) + "%");

        // 7.1 Các hoạt động
        dong("");
        dong("7.1. CÁC HOẠT ĐỘNG KHẮC PHỤC");
        int stt = 0;
        for (HoatDongKhacPhuc h : dsHoatDong.values()) {
            stt++;
            inHoatDong(tt, h, stt);
        }

        // 7.2 Chi từ nguồn nào
        dong("");
        dong("7.2. KINH PHÍ CHI TỪ NGUỒN NÀO");
        dong(String.format("   %-7s %-10s %-18s %16s %16s %16s", "Mã", "Nhóm", "Mục đích sử dụng", "Huy động", "Đã chi", "Còn lại"));
        for (NguonKinhPhi n : dsNguon.values()) {
            dong(String.format("   %-7s %-10s %-18s %16s %16s %16s", n.getMaNguon(), tenNhomNguon(n.getNhomNguon()),
                    tenMucDichSuDung(n.getMucDichSuDung()), so(n.getTongSoTien()),
                    so(tk.chiTheoNguon().get(n.getMaNguon())), so(tk.conLaiTheoNguon().get(n.getMaNguon()))));
        }

        dong("");
        dong("   Chi theo nhóm nguồn:");
        for (Map.Entry<NhomNguon, Long> e : tk.chiTheoNhomNguon().entrySet()) {
            double tyLe = tk.tongChi() == 0 ? 0.0 : e.getValue() * 100.0 / tk.tongChi();
            dong(String.format("   %-12s %18s %9.2f%% tổng chi", tenNhomNguon(e.getKey()), so(e.getValue()), tyLe));
        }

        // 7.3 Chi theo đối tượng
        dong("");
        dong("7.3. CHI THEO LOẠI HOẠT ĐỘNG, KHU VỰC, ĐỊA PHƯƠNG");
        dong("   Theo loại hoạt động:");
        for (Map.Entry<LoaiHoatDong, Long> e : tk.chiTheoLoai().entrySet()) {
            dong(String.format("   %-18s %18s", tenLoaiHoatDong(e.getKey()), so(e.getValue())));
        }
        dong("   Theo khu vực:");
        for (Map.Entry<String, Long> e : tk.chiTheoKhuVuc().entrySet()) {
            dong(String.format("   %-6s %-14s %18s", e.getKey(), dsKV.get(e.getKey()).getXaPhuong(), so(e.getValue())));
        }
        dong("   Theo địa phương (tỉnh/thành):");
        for (Map.Entry<String, Long> e : tk.chiTheoDiaPhuong().entrySet()) {
            dong(String.format("   %-21s %18s", e.getKey(), so(e.getValue())));
        }

        // 7.4 Tiến độ
        dong("");
        dong("7.4. TIẾN ĐỘ HOẠT ĐỘNG");
        for (Map.Entry<TrangThaiKhacPhuc, Integer> e : tk.demHoatDongTheoTrangThai().entrySet()) {
            dong("   " + tenTrangThaiKhacPhuc(e.getKey()) + ": " + e.getValue() + " hoạt động");
        }
        List<String> vuot = tk.danhSachHoatDongVuotDuKien();
        dong("   Hoạt động chi vượt kinh phí dự kiến (" + vuot.size() + "):");
        for (String maHD : vuot) {
            HoatDongKhacPhuc h = dsHoatDong.get(maHD);
            dong("      - " + maHD + ": dự kiến " + tien(h.getKinhPhiDuKien()) + ", đã chi " + tien(h.tongChi())
                    + ", vượt " + tien(h.tongChi() - h.getKinhPhiDuKien()));
        }

        // 7.5 Đánh giá mức hỗ trợ hộ dân
        dong("");
        dong("7.5. ĐÁNH GIÁ MỨC HỖ TRỢ HỘ DÂN");
        dong(String.format("   %-6s %-22s %-14s %14s %14s  %s", "Mã hộ", "Chủ hộ", "Mức thiệt hại", "Nhu cầu", "Đã hỗ trợ",
                "Mức đáp ứng"));
        for (BanGhiThietHaiHoDan bg : tt.getDsBanGhi().values()) {
            String maHo = bg.getHoDan().getMaHo();
            dong(String.format("   %-6s %-22s %-14s %14s %14s  %s", maHo, bg.getHoDan().getChuHo().getHoTen(),
                    tenMucDo(bg.danhGiaMucDo()), so(dg.nhuCauHo(maHo)), so(dg.daHoTroHo(maHo)),
                    tenMucDapUng(dg.mucDapUngHo(maHo))));
        }

        // 7.6 Khu vực
        dong("");
        dong("7.6. ĐÁNH GIÁ MỨC HỖ TRỢ KHU VỰC (nhu cầu gồm các hộ và hạ tầng)");
        dong(String.format("   %-6s %-14s %-14s %16s %16s  %s", "Mã KV", "Xã/Phường", "Mức thiệt hại", "Nhu cầu",
                "Đã hỗ trợ", "Mức đáp ứng"));
        for (KhuVuc kv : dsKV.values()) {
            String maKV = kv.getMaKV();
            dong(String.format("   %-6s %-14s %-14s %16s %16s  %s", maKV, kv.getXaPhuong(), tenMucDo(tt.danhGiaKhuVuc(maKV)),
                    so(dg.nhuCauKhuVuc(maKV)), so(dg.daHoTroKhuVuc(maKV)), tenMucDapUng(dg.mucDapUngKhuVuc(maKV))));
        }

        // 7.7 Địa phương
        dong("");
        dong("7.7. ĐÁNH GIÁ MỨC HỖ TRỢ ĐỊA PHƯƠNG (tỉnh/thành phố)");
        dong(String.format("   %-14s %16s %16s  %s", "Địa phương", "Nhu cầu", "Đã hỗ trợ", "Mức đáp ứng"));
        for (String tinh : dg.danhSachDiaPhuong()) {
            dong(String.format("   %-14s %16s %16s  %s", tinh, so(dg.nhuCauDiaPhuong(tinh)), so(dg.daHoTroDiaPhuong(tinh)),
                    tenMucDapUng(dg.mucDapUngDiaPhuong(tinh))));
        }

        // 7.8 Danh sách theo mức đáp ứng
        dong("");
        dong("7.8. DANH SÁCH THEO MỨC ĐÁP ỨNG");
        for (MucDapUng m : MucDapUng.values()) {
            List<HoDan> dsHo = dg.danhSachHoTheoMucDapUng(m);
            dong("   Mức \"" + tenMucDapUng(m) + "\"");
            dong("      Hộ dân (" + dsHo.size() + "):");
            for (HoDan ho : dsHo) {
                long thieu = Math.max(0, dg.nhuCauHo(ho.getMaHo()) - dg.daHoTroHo(ho.getMaHo()));
                dong("         - " + ho.getMaHo() + " | chủ hộ " + ho.getChuHo().getHoTen() + " | còn thiếu " + tien(thieu));
            }
            List<String> dsKVm = new ArrayList<>();
            for (KhuVuc kv : dg.danhSachKhuVucTheoMucDapUng(m)) {
                dsKVm.add(kv.getMaKV() + " (" + kv.getXaPhuong() + ")");
            }
            dong("      Khu vực (" + dsKVm.size() + "): " + (dsKVm.isEmpty() ? "không có" : String.join(", ", dsKVm)));
            List<String> dsDP = dg.danhSachDiaPhuongTheoMucDapUng(m);
            dong("      Địa phương (" + dsDP.size() + "): " + (dsDP.isEmpty() ? "không có" : String.join(", ", dsDP)));
        }
    }

    // In một hoạt động khắc phục: thông tin chung, đối tượng, tiến độ, chi từ nguồn nào
    private static void inHoatDong(ThienTai tt, HoatDongKhacPhuc h, int stt) {

        KhuVuc kv = tt.getDsKhuVuc().get(h.getMaKV());
        String doiTuong;
        if (h.getLoai() == LoaiHoatDong.HO_TRO_HO_DAN) {
            HoDan ho = kv.getDsHoDan().get(h.getMaHo());
            doiTuong = "Hộ " + h.getMaHo() + " (chủ hộ " + ho.getChuHo().getHoTen() + ")";
        } else {
            doiTuong = "Cơ sở vật chất " + h.getMaCSVC() + " (" + tenLoaiCSVC(kv.timCoSoVatChat(h.getMaCSVC()).getLoaiCSVC()) + ")";
        }

        dong("");
        dong("   [" + stt + "] Mã hoạt động    : " + h.getMaHoatDong());
        dong("       Tên hoạt động    : " + h.getTen());
        dong("       Loại             : " + tenLoaiHoatDong(h.getLoai()));
        dong("       Khu vực          : " + kv.getMaKV() + " (" + kv.getXaPhuong() + ", " + kv.getTinhThanhPho() + ")");
        dong("       Đối tượng        : " + doiTuong);
        dong("       Trạng thái       : " + tenTrangThaiKhacPhuc(h.getTrangThai()));
        dong("       Kinh phí dự kiến : " + tien(h.getKinhPhiDuKien()));
        dong("       Đã chi           : " + tien(h.tongChi())
                + (h.tongChi() > h.getKinhPhiDuKien() ? " (vượt dự kiến " + tien(h.tongChi() - h.getKinhPhiDuKien()) + ")" : ""));
        if (h.getChiTheoNguon().isEmpty()) {
            dong("       Chi từ nguồn     : chưa chi");
        }
        for (Map.Entry<String, Long> e : h.getChiTheoNguon().entrySet()) {
            dong("       Chi từ nguồn     : " + e.getKey() + " - " + tien(e.getValue()));
        }
    }

    private static String tenLoaiHoatDong(LoaiHoatDong l) {
        switch (l) {
            case HO_TRO_HO_DAN:    return "Hỗ trợ hộ dân";
            case SUA_CHUA_HA_TANG: return "Sửa chữa hạ tầng";
            case XAY_MOI_HA_TANG:  return "Xây mới hạ tầng";
            default:               return l.name();
        }
    }

    private static String tenTrangThaiKhacPhuc(TrangThaiKhacPhuc t) {
        switch (t) {
            case CHUA_BAT_DAU:   return "Chưa bắt đầu";
            case DANG_THUC_HIEN: return "Đang thực hiện";
            case HOAN_THANH:     return "Hoàn thành";
            default:             return t.name();
        }
    }

    private static String tenMucDichSuDung(MucDichSuDung m) {
        switch (m) {
            case KHONG_CHI_DINH:   return "Không chỉ định";
            case HO_TRO_NGUOI_DAN: return "Chỉ hộ dân";
            case CO_SO_HA_TANG:    return "Chỉ hạ tầng";
            default:               return m.name();
        }
    }

    private static String tenMucDapUng(MucDapUng m) {
        switch (m) {
            case CHUA_HO_TRO: return "Chưa hỗ trợ";
            case MOT_PHAN:    return "Hỗ trợ một phần";
            case DAY_DU:      return "Đáp ứng đủ";
            default:          return m.name();
        }
    }
}