package main;

import trangthai.CapDanhGia;
import trangthai.MucDoThietHai;

/*
 * Lớp tổng hợp số liệu thiệt hại (người + vật chất) cho một hộ, một khu vực
 * hoặc toàn bộ một thiên tai. Dùng để cộng dồn số liệu và đánh giá mức độ thiệt hại.
 */
public class ThongKeThietHai {
    // Ngưỡng đánh giá: mỗi mảng gồm 4 cận trên của KHONG_DANG_KE, NHE, TRUNG_BINH, NANG; vượt cận cuối = RAT_NANG
    private static final long[] VAT_CHAT_HO   = {5_000_000L, 50_000_000L, 200_000_000L, 1_000_000_000L};
    private static final long[] VAT_CHAT_KV   = {100_000_000L, 1_000_000_000L, 10_000_000_000L, 50_000_000_000L};
    private static final long[] CHET_MT_HO    = {0, 0, 0, 1};     // chết + mất tích
    private static final long[] CHET_MT_KV    = {0, 0, 2, 10};
    private static final long[] BI_THUONG_HO  = {0, 0, 1, 2};
    private static final long[] BI_THUONG_KV  = {0, 5, 20, 50};

    private int soNguoiChet;
    private int soNguoiMatTich;
    private int soNguoiBiThuong;
    private long tongGiaTriNhaCua;
    private long tongGiaTriSanXuat;
    private long tongGiaTriCauDuong;
    private long tongGiaTriCongTrinhCong;

    //  ghi dữ liệu (các lớp thiệt hại gọi để đẩy số liệu vào thống kê) 

    //Ghi (đặt lại) số người chết, mất tích, bị thương vào bản thống kê
    public void capNhatSoNguoi(int chet, int matTich, int biThuong) {
        this.soNguoiChet = chet;
        this.soNguoiMatTich = matTich;
        this.soNguoiBiThuong = biThuong;
    }

    //Cộng thêm giá trị thiệt hại nhà cửa vào tổng nhà cửa
    public void themGiaTriNhaCua(long v) { tongGiaTriNhaCua += v; }

    //Cộng thêm giá trị thiệt hại sản xuất vào tổng sản xuất
    public void themGiaTriSanXuat(long v) { tongGiaTriSanXuat += v; }

    //Cộng thêm giá trị thiệt hại cầu đường vào tổng cầu đường
    public void themGiaTriCauDuong(long v) { tongGiaTriCauDuong += v; }

    //Cộng thêm giá trị thiệt hại công trình công vào tổng công trình công. 
    public void themGiaTriCongTrinhCong(long v) { tongGiaTriCongTrinhCong += v; }

    //Cộng dồn toàn bộ số liệu của một bản thống kê khác vào bản này (bỏ qua nếu null)
    public void cong(ThongKeThietHai tk) {
        if (tk == null) return;
        soNguoiChet += tk.soNguoiChet;
        soNguoiMatTich += tk.soNguoiMatTich;
        soNguoiBiThuong += tk.soNguoiBiThuong;
        tongGiaTriNhaCua += tk.tongGiaTriNhaCua;
        tongGiaTriSanXuat += tk.tongGiaTriSanXuat;
        tongGiaTriCauDuong += tk.tongGiaTriCauDuong;
        tongGiaTriCongTrinhCong += tk.tongGiaTriCongTrinhCong;
    }

    // ----- tính toán -----

    //Tổng thiệt hại tài sản của người dân = nhà cửa + sản xuất
    public long tongGiaTriTaiSanDan() { return tongGiaTriNhaCua + tongGiaTriSanXuat; }

    //Tổng thiệt hại cơ sở vật chất nhà nước = cầu đường + công trình công
    public long tongGiaTriCoSoVatChatNN() { 
    	return tongGiaTriCauDuong + tongGiaTriCongTrinhCong; 
    	}

    //Tổng thiệt hại vật chất = tài sản dân + cơ sở vật chất nhà nước
    public long tongGiaTriVatChat() { 
    	return tongGiaTriTaiSanDan() + tongGiaTriCoSoVatChatNN(); 
    }

    //Tổng số người bị ảnh hưởng = chết + mất tích + bị thương
    public int tongSoNguoiBiAnhHuong() { 
    	return soNguoiChet + soNguoiMatTich + soNguoiBiThuong; 
   }

    /*
     * Đánh giá mức độ thiệt hại theo cấp (hộ dân hoặc khu vực).
     * Chấm ba tiêu chí độc lập (giá trị vật chất, số chết + mất tích, số bị thương)
     * theo bảng ngưỡng của cấp đó, rồi lấy mức cao nhất trong ba tiêu chí.
     */
    public MucDoThietHai danhGiaMucDo(CapDanhGia cap) {
        boolean ho = (cap == CapDanhGia.HO_DAN);
        int a = bac(tongGiaTriVatChat(), ho ? VAT_CHAT_HO : VAT_CHAT_KV);
        int b = bac(soNguoiChet + soNguoiMatTich, ho ? CHET_MT_HO : CHET_MT_KV);
        int c = bac(soNguoiBiThuong, ho ? BI_THUONG_HO : BI_THUONG_KV);
        return MucDoThietHai.values()[Math.max(a, Math.max(b, c))];
    }

    //Trả về bậc (0..4) của một giá trị: bậc đầu tiên có cận trên >= giá trị; vượt hết thì là 4 (RAT_NANG)
    private static int bac(long giaTri, long[] nguong) {
        for (int i = 0; i < nguong.length; i++) if (giaTri <= nguong[i]) return i;
        return nguong.length;
    }

	public int getSoNguoiChet() {
		return soNguoiChet;
	}

	public int getSoNguoiMatTich() {
		return soNguoiMatTich;
	}

	public int getSoNguoiBiThuong() {
		return soNguoiBiThuong;
	}

	public long getTongGiaTriNhaCua() {
		return tongGiaTriNhaCua;
	}

	public long getTongGiaTriSanXuat() {
		return tongGiaTriSanXuat;
	}

	public long getTongGiaTriCauDuong() {
		return tongGiaTriCauDuong;
	}

	public long getTongGiaTriCongTrinhCong() {
		return tongGiaTriCongTrinhCong;
	}

   
}

