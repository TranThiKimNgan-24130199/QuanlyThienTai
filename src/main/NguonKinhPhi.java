package main;
//Người thực hiện : Trần Thị Kim Ngân
import trangthai.NhomNguon;

/*
 * Nguồn kinh phí khắc phục một thiên tai (lớp cha trừu tượng)
 * Mỗi nguồn gắn với đúng một thiên tai (maTT) và cộng dồn các lần đóng góp
 */
public abstract class NguonKinhPhi {
    protected String maNguon;
    protected String tenNguon;
    protected String maTT;          // mã thiên tai mà nguồn này đóng góp cho
    protected long tongSoTien;      // tổng số tiền đã đóng góp (VNĐ), dùng long thay vì double để tránh sai số
    protected int soLanDongGop;     // số lần đóng góp

    /*
     * Tạo nguồn kinh phí mới, chưa có đóng góp nào (tongSoTien = 0, soLanDongGop = 0).
     * Ném IllegalArgumentException nếu mã nguồn, tên nguồn hoặc mã thiên tai rỗng.
     */
    protected NguonKinhPhi(String maNguon, String tenNguon, String maTT) {
        if (khongRong(maNguon) == false || khongRong(tenNguon) == false || khongRong(maTT) == false) {
            throw new IllegalArgumentException("Ma nguon, ten nguon va ma thien tai khong duoc rong");
        }
        this.maNguon = maNguon;
        this.tenNguon = tenNguon;
        this.maTT = maTT;
    }

    /*
     * Ghi nhận thêm một lần đóng góp: cộng soTien vào tổng số tiền và tăng số lần đóng góp lên 1
     * Trả về false (không thay đổi gì) nếu số tiền không dương hoặc cộng vào sẽ bị tràn kiểu long
     */
    public boolean themDongGop(long soTien) {
        if (soTien <= 0 || tongSoTien > Long.MAX_VALUE - soTien) {
            return false;
        }
        tongSoTien += soTien;
        soLanDongGop++;
        return true;
    }

    /*
     * Kiểm tra dữ liệu chung hợp lệ: các mã/tên không rỗng, số tiền và số lần không âm
     * và nhất quán (chưa có lần đóng góp nào thì tổng số tiền phải bằng 0)
     * Lớp con gọi super.kiemTraHopLe() rồi kiểm tra thêm dữ liệu riêng
     */
    public boolean kiemTraHopLe() {
        return khongRong(maNguon) && khongRong(tenNguon) && khongRong(maTT)
                && tongSoTien >= 0 && soLanDongGop >= 0
                && (soLanDongGop > 0 || tongSoTien == 0);
    }

    //Mỗi lớp con trả về nhóm nguồn của mình (dùng để thống kê theo nhóm)
    public abstract NhomNguon getNhomNguon();

    // Kiểm tra chuỗi khác null và không chỉ gồm khoảng trắng
    protected static boolean khongRong(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public String getMaNguon() {
        return maNguon;
    }

    public String getTenNguon() {
        return tenNguon;
    }

    public String getMaTT() {
        return maTT;
    }

    public long getTongSoTien() {
        return tongSoTien;
    }

    public int getSoLanDongGop() {
        return soLanDongGop;
    }
}
