package main;
import java.time.LocalDateTime;
import java.util.ArrayList;

import trangthai.CapDanhGia;
import trangthai.MucDoThietHai;
import trangthai.TrangThaiNguoi;

//Bản ghi thiệt hại của một hộ dân trong một thiên tai (gồm thiệt hại tài sản; thiệt hại người lấy từ trạng thái thành viên hộ)
public class BanGhiThietHaiHoDan {
    private String maBanGhi;
    private LocalDateTime thoiGianGhiNhan;
    private HoDan hoDan;                                              // association 1
    private ArrayList<ThietHaiTaiSanDan> dsThietHaiTaiSan = new ArrayList<>(); // composition 0..*

    //Tạo bản ghi cho một hộ dân với mã và thời điểm ghi nhận; danh sách thiệt hại tài sản ban đầu rỗng
    public BanGhiThietHaiHoDan(String maBanGhi, LocalDateTime thoiGianGhiNhan, HoDan hoDan) {
        this.maBanGhi = maBanGhi;
        this.thoiGianGhiNhan = thoiGianGhiNhan;
        this.hoDan = hoDan;
    }

    //Đổi thời điểm ghi nhận của bản ghi
    public void capNhatThoiGianGhiNhan(LocalDateTime thoiGian) { 
    	this.thoiGianGhiNhan = thoiGian; 
    }

    /*
     * Thêm một thiệt hại tài sản vào bản ghi. Trả về false nếu thiệt hại null, không hợp lệ,
     * hoặc bản ghi đã có thiệt hại cùng loại (mỗi loại nhà cửa / sản xuất chỉ có tối đa một mục).
     */
    public boolean capNhatThietHaiTaiSan(ThietHaiTaiSanDan thietHai) {
        if (thietHai == null || !thietHai.kiemTraHopLe()) return false;
        for (ThietHaiTaiSanDan t : dsThietHaiTaiSan)
            if (t.getClass() == thietHai.getClass()) return false;
        return dsThietHaiTaiSan.add(thietHai);
    }

    //Kiểm tra bản ghi hợp lệ: có mã, thời gian, hộ dân và mọi thiệt hại tài sản bên trong đều hợp lệ
    public boolean kiemTraHopLe() {
        if (maBanGhi == null || thoiGianGhiNhan == null || hoDan == null) return false;
        for (ThietHaiTaiSanDan t : dsThietHaiTaiSan) if (!t.kiemTraHopLe()) return false;
        return true;
    }

    /*
     * Tính thống kê riêng của hộ: đếm số thành viên chết / mất tích / bị thương theo trạng thái
     * trong HoDan, rồi cộng giá trị từng thiệt hại tài sản vào thống kê.
     */
    public ThongKeThietHai tinhThongKe() {
        ThongKeThietHai tk = new ThongKeThietHai();
        tk.capNhatSoNguoi(hoDan.demNguoiTheoTrangThai(TrangThaiNguoi.TU_VONG),
                          hoDan.demNguoiTheoTrangThai(TrangThaiNguoi.MAT_TICH),
                          hoDan.demNguoiTheoTrangThai(TrangThaiNguoi.BI_THUONG));
        for (ThietHaiTaiSanDan t : dsThietHaiTaiSan) t.capNhatThongTinThongKe(tk);
        return tk;
    }

    //Đánh giá mức độ thiệt hại của hộ: lấy thống kê của hộ rồi chấm theo bảng ngưỡng cấp HO_DAN
    public MucDoThietHai danhGiaMucDo() { return tinhThongKe().danhGiaMucDo(CapDanhGia.HO_DAN); }

    public String getMaBanGhi() { return maBanGhi; }
    public LocalDateTime getThoiGianGhiNhan() { return thoiGianGhiNhan; }
    public HoDan getHoDan() { return hoDan; }
    public ArrayList<ThietHaiTaiSanDan> getDsThietHaiTaiSan() { return dsThietHaiTaiSan; }
}

