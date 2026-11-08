
package main;


import java.util.HashMap;

public class KhuVuc {

   
    private String maKV;
    private String tinhThanhPho;
    private String quanHuyen;
    private String xaPhuong;

    // Quan ly cac ho dan thuoc khu vuc, su dung ma ho lam khoa
    private HashMap<String, HoDan> dsHoDan = new HashMap<>();

    // Quan ly cac co so vat chat Nha nuoc thuoc khu vuc
    private HashMap<String, ThietHaiCoSoVatChat_NN> dsThietHaiCoSoVatChatNN
    = new HashMap<>();
    //cập nhật thông tin
    public KhuVuc(String maKV, String tinhThanhPho,
                  String quanHuyen, String xaPhuong) {

        if (maKV == null || maKV.trim().isEmpty()) {
            throw new IllegalArgumentException("Ma khu vuc khong hop le");
        }

        this.maKV = maKV;
        capNhatThongTin(tinhThanhPho, quanHuyen, xaPhuong);
    }

   

    public void capNhatThongTin(String tinhThanhPho,
                                String quanHuyen, String xaPhuong) {
    	  if (tinhThanhPho == null || tinhThanhPho.trim().isEmpty()
    	            || quanHuyen == null || quanHuyen.trim().isEmpty()
    	            || xaPhuong == null || xaPhuong.trim().isEmpty()) {

    	        throw new IllegalArgumentException("Thong tin khu vuc khong hop le");
    	    }

        this.tinhThanhPho = tinhThanhPho;
        this.quanHuyen = quanHuyen;
        this.xaPhuong = xaPhuong;
    }
    //quản lý hộ dân trong khu vực
    // Them mot ho dan vao khu vuc
    public boolean themHoDan(HoDan hoDan) {

        if (hoDan == null) return false;

        String maHo = hoDan.getMaHo();

        if (maHo == null || maHo.trim().isEmpty()) return false;

        // Kiem tra ho dan da ton tai trong khu vuc chua
        if (dsHoDan.containsKey(maHo)) return false;

        // Khong cho mot ho dan thuoc hai khu vuc khac nhau
        if (hoDan.getKhuVuc() != null) return false;

        dsHoDan.put(maHo, hoDan);

        // Gan khu vuc cho ho dan
        hoDan.ganKhuVuc(this);

        return true;
    }

    // Xoa ho dan theo ma ho
    public boolean xoaHoDan(String maHo) {

        if (maHo == null || maHo.trim().isEmpty()) return false;

        HoDan hoDan = dsHoDan.remove(maHo);

        if (hoDan == null) return false;

        // Huy lien ket giua ho dan va khu vuc
        hoDan.ganKhuVuc(null);

        return true;
    }

    // Tim ho dan theo ma ho
    public HoDan timHoDan(String maHo) {

        if (maHo == null || maHo.trim().isEmpty()) return null;

        return dsHoDan.get(maHo);
    }

    // Dem so ho dan trong khu vuc
    public int demSoHoDan() {
        return dsHoDan.size();
    }
    //quản lý cả cơ sở vật chất của nhà nước theo từng khu vực
    // Them mot co so vat chat Nha nuoc vao khu vuc
 // Thêm bản ghi thiệt hại cơ sở vật chất vào khu vực
    public boolean themCoSoVatChat(ThietHaiCoSoVatChat_NN coSo) {
        if (coSo == null) {
            return false;
        }

        String maCSVC = coSo.getMaCSVC();

        if (maCSVC == null || maCSVC.trim().isEmpty()) {
            return false;
        }
        // Kiểm tra dữ liệu thiệt hại hợp lệ
        if (!coSo.kiemTraHopLe()) {
            return false;
        }
        if (dsThietHaiCoSoVatChatNN.containsKey(maCSVC)) {
            return false;
        }

        dsThietHaiCoSoVatChatNN.put(maCSVC, coSo);
        return true;
    }

    public HashMap<String, HoDan> getDsHoDan() {
		return dsHoDan;
	}



	public void setDsHoDan(HashMap<String, HoDan> dsHoDan) {
		this.dsHoDan = dsHoDan;
	}



	// Xóa bản ghi theo mã cơ sở vật chất
    public boolean xoaCoSoVatChat(String maCSVC) {
        if (maCSVC == null || maCSVC.trim().isEmpty()) {
            return false;
        }

        return dsThietHaiCoSoVatChatNN.remove(maCSVC) != null;
    }

    // Tìm bản ghi theo mã cơ sở vật chất
    public ThietHaiCoSoVatChat_NN timCoSoVatChat(String maCSVC) {
        if (maCSVC == null || maCSVC.trim().isEmpty()) {
            return null;
        }

        return dsThietHaiCoSoVatChatNN.get(maCSVC);
    }

    // Đếm số bản ghi thiệt hại cơ sở vật chất
    public int demSoCoSoVatChat() {
        return dsThietHaiCoSoVatChatNN.size();
    }

    public void setDsThietHaiCoSoVatChatNN(HashMap<String, ThietHaiCoSoVatChat_NN> dsThietHaiCoSoVatChatNN) {
		this.dsThietHaiCoSoVatChatNN = dsThietHaiCoSoVatChatNN;
	}



	// Trả về bản sao danh sách bản ghi
    public HashMap<String, ThietHaiCoSoVatChat_NN>
            getDsThietHaiCoSoVatChatNN() {
        return new HashMap<>(dsThietHaiCoSoVatChatNN);
    }
  
    public String getMaKV() {
        return maKV;
    }

    public String getTinhThanhPho() {
        return tinhThanhPho;
    }

    public String getQuanHuyen() {
        return quanHuyen;
    }

    public String getXaPhuong() {
        return xaPhuong;
    }

}
