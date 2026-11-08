package main;
//Người thực hiện: Trần Thị Kim Ngân
public class ThietHaiNhaCua extends ThietHaiTaiSanDan {
    private int soNhaSapHoanToan;
    private int soNhaHuHong;

    /*
     * Cập nhật số nhà sập hoàn toàn, số nhà hư hỏng và tổng giá trị thiệt hại
     * Ném IllegalArgumentException nếu số nhà âm (giá trị tiền được kiểm tra ở lớp cha)
     */
    public void capNhatVeCua(int soNhaSapHoanToan, int soNhaHuHong, long tongGiaTri) {
        if (soNhaSapHoanToan < 0 || soNhaHuHong < 0)
            throw new IllegalArgumentException("Số nhà không được âm");
        this.soNhaSapHoanToan = soNhaSapHoanToan;
        this.soNhaHuHong = soNhaHuHong;
        capNhatGiaTriThietHai(tongGiaTri);
    }

    //Hợp lệ khi điều kiện của lớp cha đúng và số nhà sập / hư hỏng không âm
    @Override 
    public boolean kiemTraHopLe() {
        return super.kiemTraHopLe() && soNhaSapHoanToan >= 0 && soNhaHuHong >= 0;
    }

    //Cộng giá trị thiệt hại nhà cửa vào mục "nhà cửa" của bản thống kê
    @Override 
    public void capNhatThongTinThongKe(ThongKeThietHai tk) {
        tk.themGiaTriNhaCua(tongGiaTriThietHai);
    }

    public int getSoNhaSapHoanToan() { 
    	return soNhaSapHoanToan; 
    }
    public int getSoNhaHuHong() { 
    	return soNhaHuHong; 
    }
}

