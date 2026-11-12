package main;

public abstract class ThietHaiVatChat {
    protected long tongGiaTriThietHai;

    //Trả về tổng giá trị thiệt hại hiện lưu của đối tượng này
    public long tinhTongThietHai() { 
    	return tongGiaTriThietHai; 
    }

    //Gán giá trị thiệt hại mới; ném IllegalArgumentException nếu số tiền âm
    public void capNhatGiaTriThietHai(long soTien) {
        if (soTien < 0) throw new IllegalArgumentException("Giá trị thiệt hại không được âm");
        this.tongGiaTriThietHai = soTien;
    }

    //Kiểm tra dữ liệu hợp lệ: giá trị thiệt hại không âm. Lớp con mở rộng thêm điều kiện riêng
    public boolean kiemTraHopLe() { 
    	return tongGiaTriThietHai >= 0; 
    }

    //Mỗi loại thiệt hại tự đẩy giá trị của mình vào đúng mục của bản thống kê
    public abstract void capNhatThongTinThongKe(ThongKeThietHai tk);
}

