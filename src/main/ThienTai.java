package main;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import trangthai.*;

public class ThienTai {
    private String maTT;
    private String ten;
    private LoaiThienTai loaiThienTai;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;    // null khi chưa kết thúc
    private String moTa;
    private TrangThaiThienTai trangThai = TrangThaiThienTai.DANG_DIEN_RA;
    private HashMap<String, KhuVuc> dsKhuVuc = new HashMap<>();                
    private TreeMap<String, BanGhiThietHaiHoDan> dsBanGhi = new TreeMap<>();   

    //Tạo thiên tai mới ở trạng thái DANG_DIEN_RA, chưa có khu vực và bản ghi nào
    public ThienTai(String maTT, String ten, LoaiThienTai loaiThienTai, LocalDateTime thoiGianBatDau, String moTa) {
        if (maTT == null || maTT.trim().isEmpty())
            throw new IllegalArgumentException("Mã thiên tai không hợp lệ");
        this.maTT = maTT;
        capNhatThongTin(ten, loaiThienTai, thoiGianBatDau, moTa);
    }

    //Cập nhật thông tin mô tả của thiên tai (tên, loại, thời gian bắt đầu, mô tả); mã không đổi
    public void capNhatThongTin(String ten, LoaiThienTai loai, LocalDateTime thoiGianBatDau, String moTa) {
        if (ten == null || ten.trim().isEmpty() || loai == null || thoiGianBatDau == null)
            throw new IllegalArgumentException("Thông tin thiên tai không hợp lệ");
        this.ten = ten;
        this.loaiThienTai = loai;
        this.thoiGianBatDau = thoiGianBatDau;
        this.moTa = moTa;
    }

    /*
     * Kết thúc sự kiện: ghi thời gian kết thúc và chuyển trạng thái sang DA_KET_THUC
     * Trả về false nếu đã kết thúc trước đó, thời gian null hoặc sớm hơn thời gian bắt đầu
     */
    public boolean ketThucSuKien(LocalDateTime thoiGian) {
        if (trangThai == TrangThaiThienTai.DA_KET_THUC || thoiGian == null|| thoiGian.isBefore(thoiGianBatDau)) 
        	return false;
        this.thoiGianKetThuc = thoiGian;
        this.trangThai = TrangThaiThienTai.DA_KET_THUC;
        return true;
    }

    //Thêm khu vực bị ảnh hưởng vào thiên tai. Trả về false nếu khu vực null hoặc đã có (trùng mã)
    public boolean themKhuVuc(KhuVuc kv) {
        if (kv == null || dsKhuVuc.containsKey(kv.getMaKV())) 
        	return false;
        dsKhuVuc.put(kv.getMaKV(), kv);
        return true;
    }
    /*
     * Thêm bản ghi thiệt hại hộ dân. Chỉ nhận khi: sự kiện còn diễn ra, bản ghi hợp lệ
     * không trùng mã, khu vực của hộ đã thuộc thiên tai này, và hộ chưa có bản ghi nào
     * (mỗi hộ một bản ghi để không cộng trùng số người bị ảnh hưởng)
     */
    public boolean themBanGhi(BanGhiThietHaiHoDan bg) {
        if (bg == null || trangThai == TrangThaiThienTai.DA_KET_THUC || !bg.kiemTraHopLe()
                || dsBanGhi.containsKey(bg.getMaBanGhi())) 
        	return false;
        HoDan ho = bg.getHoDan();
        if (ho.getKhuVuc() == null || !dsKhuVuc.containsKey(ho.getKhuVuc().getMaKV())) 
        	return false;
        for (BanGhiThietHaiHoDan b : dsBanGhi.values())
            if (b.getHoDan().getMaHo().equals(ho.getMaHo())) 
            	return false;
        dsBanGhi.put(bg.getMaBanGhi(), bg);
        return true;
    }

    // ---------- tổng hợp ----------

    //Tổng thiệt hại vật chất (tài sản dân + cơ sở vật chất nhà nước) của toàn thiên tai
    public long tinhTongThietHaiVatChat() { return thongKeToanBo().tongGiaTriVatChat(); }

    // số người chết của toàn thiên tai
    public int tinhTongNguoiChet() { return thongKeToanBo().getSoNguoiChet(); }

    //Tổng số người bị thương của toàn thiên tai
    public int tinhTongNguoiBiThuong() { return thongKeToanBo().getSoNguoiBiThuong(); }

    //Tổng số người mất tích của toàn thiên tai
    public int tinhTongNguoiMatTich() { return thongKeToanBo().getSoNguoiMatTich(); }

    /*
     * Thống kê toàn thiên tai: cộng thống kê của từng bản ghi hộ dân
     * và thiệt hại cơ sở vật chất (chỉ khoản ghi cho thiên tai này) của mọi khu vực thuộc thiên tai
     */
    public ThongKeThietHai thongKeToanBo() {

        ThongKeThietHai tk = new ThongKeThietHai();

        for (BanGhiThietHaiHoDan b : dsBanGhi.values())
            tk.cong(b.tinhThongKe());

        for (KhuVuc kv : dsKhuVuc.values()) {
            for (ThietHaiCoSoVatChat_NN t : kv.getDsThietHaiCoSoVatChatNN().values())
                if (maTT.equals(t.getMaTT()))   // chỉ tính thiệt hại ghi cho chính thiên tai này
                    t.capNhatThongTinThongKe(tk);
        }

        return tk;
    }

    /*
     * Thống kê riêng một khu vực: cộng thống kê các bản ghi của hộ thuộc khu vực đó
     * và thiệt hại cơ sở vật chất của khu vực (chỉ khoản ghi cho thiên tai này). Ném IllegalArgumentException nếu khu vực không thuộc thiên tai
     */
    public ThongKeThietHai thongKeKhuVuc(String maKV) {

        KhuVuc kv = dsKhuVuc.get(maKV);

        if (kv == null)
            throw new IllegalArgumentException("Khu vực không thuộc thiên tai: " + maKV);

        ThongKeThietHai tk = new ThongKeThietHai();

        for (BanGhiThietHaiHoDan b : dsBanGhi.values()) {
            if (b.getHoDan().getKhuVuc() == kv)
                tk.cong(b.tinhThongKe());
        }

        for (ThietHaiCoSoVatChat_NN t : kv.getDsThietHaiCoSoVatChatNN().values()) {
            if (!maTT.equals(t.getMaTT()))      // bỏ qua thiệt hại ghi cho thiên tai khác
                continue;
            t.capNhatThongTinThongKe(tk);
        }

        return tk;
    }
    // ---------- đánh giá ----------

    //Đánh giá mức độ thiệt hại của một khu vực: lấy thống kê khu vực rồi chấm theo bảng ngưỡng cấp KHU_VUC
    public MucDoThietHai danhGiaKhuVuc(String maKV) {
        return thongKeKhuVuc(maKV).danhGiaMucDo(CapDanhGia.KHU_VUC);
    }

    /** Lọc danh sách các hộ dân có mức độ thiệt hại đúng bằng mức được chọn. */
    public List<HoDan> danhSachHoTheoMucDo(MucDoThietHai mucDo) {
        List<HoDan> kq = new ArrayList<>();
        for (BanGhiThietHaiHoDan b : dsBanGhi.values())
            if (b.danhGiaMucDo() == mucDo) kq.add(b.getHoDan());
        return kq;
    }

    //Phân nhóm toàn bộ hộ dân theo mức độ thiệt hại; mọi mức đều có khóa (danh sách rỗng nếu không có hộ)
    public EnumMap<MucDoThietHai, List<HoDan>> nhomHoTheoMucDo() {
        EnumMap<MucDoThietHai, List<HoDan>> kq = new EnumMap<>(MucDoThietHai.class);
        for (MucDoThietHai m : MucDoThietHai.values()) kq.put(m, new ArrayList<>());
        for (BanGhiThietHaiHoDan b : dsBanGhi.values()) kq.get(b.danhGiaMucDo()).add(b.getHoDan());
        return kq;
    }

    //Lọc danh sách các khu vực của thiên tai có mức độ thiệt hại đúng bằng mức được chọn
    public List<KhuVuc> danhSachKhuVucTheoMucDo(MucDoThietHai mucDo) {
        List<KhuVuc> kq = new ArrayList<>();
        for (KhuVuc kv : dsKhuVuc.values())
            if (danhGiaKhuVuc(kv.getMaKV()) == mucDo) kq.add(kv);
        return kq;
    }

	public String getMaTT() {
		return maTT;
	}

	public String getTen() {
		return ten;
	}

	public LoaiThienTai getLoaiThienTai() {
		return loaiThienTai;
	}

	public LocalDateTime getThoiGianBatDau() {
		return thoiGianBatDau;
	}

	public LocalDateTime getThoiGianKetThuc() {
		return thoiGianKetThuc;
	}

	public String getMoTa() {
		return moTa;
	}

	public TrangThaiThienTai getTrangThai() {
		return trangThai;
	}

	public HashMap<String, KhuVuc> getDsKhuVuc() {
		// Tra ve ban sao de ben ngoai khong sua truc tiep danh sach noi bo
		return new HashMap<>(dsKhuVuc);
	}

	public TreeMap<String, BanGhiThietHaiHoDan> getDsBanGhi() {
		// Tra ve ban sao de ben ngoai khong sua truc tiep danh sach noi bo
		return new TreeMap<>(dsBanGhi);
	}

  
}
