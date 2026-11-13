package trangthai;
/*
 * MucDoDongGop: mức độ đóng góp của MỘT bên (cá nhân / tập thể / công ty)
 * dựa trên TỔNG số tiền bên đó đã góp cho thiên tai.
 *
 * Vì sao dùng enum?
 *  - Mức độ chỉ có 4 giá trị cố định, enum an toàn hơn dùng số 0..3 hay chuỗi
 *    (không thể truyền giá trị sai, switch/so sánh rõ nghĩa).
 *  - Giống cách MucDoThietHai đã làm trong dự án, nên cả nhóm đọc quen mắt.
 *
 * QUAN TRỌNG - THỨ TỰ KHAI BÁO: từ thấp đến cao (THAP -> RAT_CAO).
 * Hàm danhGia() dùng values()[i] để đổi "vị trí ngưỡng" thành "mức độ",
 * nên nếu đổi thứ tự các giá trị ở đây thì kết quả sẽ sai.
 */
public enum MucDoDongGop {
 
    THAP,         // góp ít
    TRUNG_BINH,   // góp vừa
    CAO,          // góp nhiều
    RAT_CAO;      // góp rất nhiều
 
    /*
     * BẢNG NGƯỠNG (đơn vị: đồng VND, dùng long vì số tiền lớn: công ty có thể vài tỷ,
     * vượt giới hạn của int ~ 2,147 tỷ khi cộng dồn).
     *
     * Mỗi nhóm có đúng 3 "cận trên", tương ứng 3 mức đầu:
     *   tổng <= cận[0]  -> THAP
     *   tổng <= cận[1]  -> TRUNG_BINH
     *   tổng <= cận[2]  -> CAO
     *   còn lại (vượt cận[2]) -> RAT_CAO   (mức cuối không cần cận)
     *
     * Vì sao mỗi nhóm một bảng riêng? Vì 5 triệu là khoản lớn với một cá nhân
     * nhưng rất nhỏ với một công ty, dùng chung một bảng thì không công bằng.
     *
     * Lưu ý: các con số này do nhóm tự chọn (không có quy định chính thức),
     * muốn đổi thì chỉ sửa ở ba mảng này, code phía dưới không phải đụng tới.
     *
     * static final: bảng dùng chung cho mọi lần gọi, tạo một lần duy nhất và không bị sửa.
     * private: giấu đi để bên ngoài không đổi được ngưỡng; họ chỉ gọi danhGia().
     * Chữ L cuối số để Java hiểu là long; dấu _ chỉ để dễ đọc (1_000_000 = một triệu).
     */
    private static final long[] NGUONG_CA_NHAN = { 1_000_000L, 5_000_000L, 20_000_000L };
    private static final long[] NGUONG_TAP_THE = { 10_000_000L, 50_000_000L, 200_000_000L };
    private static final long[] NGUONG_CONG_TY = { 50_000_000L, 500_000_000L, 2_000_000_000L };
 
    /*
     * Chấm mức đóng góp của một bên.
     *
     * nhom     : nhóm của bên đó (cá nhân / tập thể / công ty)
     * tongTien : TỔNG tiền bên đó đã góp (cộng tất cả các lần), phải > 0
     *
     * Vì sao là static? Hàm chỉ "tính toán từ tham số", không cần đối tượng enum nào cụ thể.
     * Gọi: MucDoDongGop.danhGia(NhomNguon.CA_NHAN, 3_000_000L)
     *
     * Ném IllegalArgumentException khi đầu vào sai (cùng phong cách validate của cả dự án),
     * để lỗi lộ ra ngay thay vì âm thầm trả kết quả sai.
     */
    public static MucDoDongGop danhGia(NhomNguon nhom, long tongTien) {
 
        // Bước 1: kiểm tra đầu vào.
        if (nhom == null) {
            throw new IllegalArgumentException("Nhóm nguồn không hợp lệ");
        }
        if (tongTien <= 0) {
            throw new IllegalArgumentException("Tổng tiền đóng góp phải lớn hơn 0");
        }
 
        // Bước 2: chọn bảng ngưỡng theo nhóm.
        long[] nguong;
        switch (nhom) {
            case CA_NHAN:
                nguong = NGUONG_CA_NHAN;
                break;
            case TAP_THE:
                nguong = NGUONG_TAP_THE;
                break;
            case CONG_TY:
                nguong = NGUONG_CONG_TY;
                break;
            default:
                // Chỉ còn NHA_NUOC: tiền nhà nước là ngân sách, không "chấm mức đóng góp".
                // Ném lỗi (thay vì trả null) để không gây NullPointerException ở chỗ khác.
                // Lớp ThongKeDongGop sẽ tự lọc bỏ nhóm này trước khi gọi hàm.
                throw new IllegalArgumentException("Nhóm " + nhom + " không được chấm mức đóng góp");
        }
 
        // Bước 3: tìm cận đầu tiên mà tổng tiền không vượt quá.
        // Dùng "<=" nên đúng bằng cận thì thuộc mức THẤP hơn
        // (vd cá nhân góp đúng 1.000.000 là THAP, 1.000.001 mới là TRUNG_BINH).
        // values()[i]: i = 0 -> THAP, 1 -> TRUNG_BINH, 2 -> CAO.
        for (int i = 0; i < nguong.length; i++) {
            if (tongTien <= nguong[i]) {
                return values()[i];
            }
        }
 
        // Bước 4: vượt cả ba cận -> mức cao nhất.
        return RAT_CAO;
    }
}