package main;
//Người thực hiện: Trần Thị Kim Ngân
public class ThietHaiSanXuat extends ThietHaiTaiSanDan {
    private double dienTichCayTrongBiThietHai;
    private int soVatNuoiBiThietHai;

    /*
     * Cập nhật diện tích cây trồng bị thiệt hại, số vật nuôi bị thiệt hại và tổng giá trị thiệt hại
     * Ném IllegalArgumentException nếu diện tích hoặc số vật nuôi âm
     */
    public void capNhatSanXuat(double dienTich, int soVatNuoi, long tongGiaTri) {
        if (dienTich < 0 || soVatNuoi < 0)
            throw new IllegalArgumentException("Diện tích / số vật nuôi không được âm");
        this.dienTichCayTrongBiThietHai = dienTich;
        this.soVatNuoiBiThietHai = soVatNuoi;
        capNhatGiaTriThietHai(tongGiaTri);
    }

    //Hợp lệ khi điều kiện của lớp cha đúng và diện tích / số vật nuôi không âm
    @Override 
    public boolean kiemTraHopLe() {
        return super.kiemTraHopLe() && dienTichCayTrongBiThietHai >= 0 && soVatNuoiBiThietHai >= 0;
    }

    //Cộng giá trị thiệt hại sản xuất vào mục "sản xuất" của bản thống kê
    @Override 
    public void capNhatThongTinThongKe(ThongKeThietHai tk) {
        tk.themGiaTriSanXuat(tongGiaTriThietHai);
    }

    public double getDienTichCayTrongBiThietHai() { 
    	return dienTichCayTrongBiThietHai; 
    	}
    public int getSoVatNuoiBiThietHai() { 
    	return soVatNuoiBiThietHai; 
    }
}

