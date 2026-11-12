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
 * Lớp chạy thử: dựng dữ liệu mẫu cho Bão số 5 rồi in một báo cáo thiệt hại đầy đủ.
 * Báo cáo gồm: thông tin thiên tai, tổng hợp, chi tiết từng khu vực
 * (cơ sở vật chất nhà nước, từng hộ dân, từng thành viên), danh sách theo mức độ.
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
}