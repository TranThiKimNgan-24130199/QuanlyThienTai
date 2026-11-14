package main;
//người thực hiện: Nguyễn Thị Hoài Lan
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import trangthai.MucDoDongGop;
import trangthai.NhomNguon;

/*
 * ThongKeDongGop: thống kê các khoản đóng góp (thu) của MỘT thiên tai.
 *
 * Cách hoạt động: nhận một danh sách NguonKinhPhi (mỗi phần tử = một bên đóng góp, đã cộng dồn
 * các lần góp; "bên" được nhận diện bằng maNguon), rồi trong constructor gom sẵn số liệu MỘT LẦN.
 * Các hàm thống kê bên dưới chỉ đọc từ số liệu đã gom, nên đơn giản và không phải duyệt lại nhiều lần.
 * QUY ƯỚC: nhóm NHA_NUOC là tiền ngân sách, không phải "đóng góp" nên KHÔNG được chấm mức,
 * không tính vào bên cao nhất / thấp nhất / trung bình mỗi bên. Nhưng vẫn được cộng vào
 * các tổng tiền, số lần góp, tỷ lệ theo nhóm (vì đó là tiền thực sự thu được).
 */
public class ThongKeDongGop {

    // Tổng số lần góp của mọi nguồn (mỗi nguồn đã đếm số lần góp của nó). Chỉ cần đếm nên lưu số.
    private int soLanDongGop;

    // Tổng tiền của từng bên, khóa là maBen.
    // TreeMap để khi duyệt luôn theo thứ tự maBen -> kết quả ổn định, dễ kiểm tra khi in ra.
    private TreeMap<String, Long> tongTheoBen = new TreeMap<>();

    // Nhóm của từng bên, khóa là maBen (một bên chỉ thuộc một nhóm)
    private TreeMap<String, NhomNguon> nhomCuaBen = new TreeMap<>();

    // Tổng tiền theo nhóm. EnumMap: khóa là enum nên nhanh, gọn và luôn duyệt đúng thứ tự khai báo.
    private EnumMap<NhomNguon, Long> tongTheoNhom = new EnumMap<>(NhomNguon.class);

    // Gom số liệu từ danh sách các lần góp.
    public ThongKeDongGop(List<NguonKinhPhi> dsNguon) {

        if (dsNguon == null) {
            throw new IllegalArgumentException("Danh sách nguồn kinh phí không hợp lệ");
        }

        // Cho mọi nhóm bắt đầu từ 0 để sau này nhóm nào chưa ai góp vẫn có khóa (giá trị 0),
        // người dùng không phải kiểm tra null.
        for (NhomNguon n : NhomNguon.values()) {
            tongTheoNhom.put(n, 0L);
        }

        for (NguonKinhPhi nguon : dsNguon) {

            if (nguon == null) {
                throw new IllegalArgumentException("Danh sách nguồn kinh phí có phần tử null");
            }

            String maBen = nguon.getMaNguon();
            NhomNguon nhom = nguon.getNhomNguon();
            long soTien = nguon.getTongSoTien();

            // Nguồn chưa góp đồng nào thì không có gì để thống kê, và sẽ làm sai
            // bên thấp nhất, trung bình, và lỗi khi chấm mức
            if (soTien <= 0) {
                throw new IllegalArgumentException("Nguồn " + maBen + " chưa có đóng góp nào");
            }

            // Một bên chỉ được thuộc một nhóm; nếu dữ liệu mâu thuẫn thì báo lỗi ngay
            // vì nếu bỏ qua thì mức đóng góp của bên đó sẽ bị tính sai.
            NhomNguon nhomDaCo = nhomCuaBen.get(maBen);
            if (nhomDaCo != null && nhomDaCo != nhom) {
                throw new IllegalArgumentException("Bên " + maBen + " thuộc nhiều nhóm khác nhau");
            }
            nhomCuaBen.put(maBen, nhom);

            // Cộng dồn vào tổng của bên (chưa có thì bắt đầu từ 0)
            tongTheoBen.put(maBen, tongTheoBen.getOrDefault(maBen, 0L) + soTien);

            // Cộng dồn vào tổng của nhóm
            tongTheoNhom.put(nhom, tongTheoNhom.get(nhom) + soTien);

            // Cộng số lần góp của nguồn này (không phải +1: một nguồn có thể góp nhiều đợt)
            soLanDongGop += nguon.getSoLanDongGop();
        }
    }

    // tổng tiền
    // Tổng tiền thu được từ mọi bên, mọi nhóm
    public long tongDongGop() {
        long tong = 0;
        for (long t : tongTheoNhom.values()) {
            tong += t;
        }
        return tong;
    }

    // Tổng tiền của từng bên. Trả về bản sao để bên ngoài không sửa được số liệu bên trong
    // (cùng nguyên tắc với các getter khác của dự án).
    public TreeMap<String, Long> tongTheoBen() {
        return new TreeMap<>(tongTheoBen);
    }

    // Tổng tiền theo nhóm (đủ mọi nhóm, nhóm chưa ai góp có giá trị 0)
    public EnumMap<NhomNguon, Long> tongTheoNhom() {
        return new EnumMap<>(tongTheoNhom);
    }

    // đếm
    // Số bên khác nhau đã góp (một bên góp nhiều lần vẫn chỉ tính 1)
    public int demSoBen() {
        return tongTheoBen.size();
    }

    // Số bên khác nhau theo từng nhóm
    public EnumMap<NhomNguon, Integer> demSoBenTheoNhom() {
        EnumMap<NhomNguon, Integer> kq = new EnumMap<>(NhomNguon.class);
        for (NhomNguon n : NhomNguon.values()) {
            kq.put(n, 0);
        }
        for (NhomNguon n : nhomCuaBen.values()) {
            kq.put(n, kq.get(n) + 1);
        }
        return kq;
    }

    // Tổng số lần góp (một bên góp 3 lần tính là 3)
    public int demSoLanDongGop() {
        return soLanDongGop;
    }

    // tỷ lệ
    // Tỷ lệ phần trăm (0..100) mỗi nhóm chiếm trong tổng tiền.
    // Chưa có khoản nào (tổng = 0) thì mọi tỷ lệ là 0.0 để tránh chia cho 0.
    public EnumMap<NhomNguon, Double> tyLeTheoNhom() {
        EnumMap<NhomNguon, Double> kq = new EnumMap<>(NhomNguon.class);
        long tong = tongDongGop();
        for (NhomNguon n : NhomNguon.values()) {
            // nhân 100.0 (số thực) để phép chia không bị cắt phần thập phân như chia số nguyên
            kq.put(n, tong == 0 ? 0.0 : tongTheoNhom.get(n) * 100.0 / tong);
        }
        return kq;
    }

    // mức đóng góp
    // Mức đóng góp của một bên, chấm trên TỔNG tiền bên đó đã góp.
    // Ném lỗi nếu không có bên này hoặc bên đó là nhà nước (không chấm mức).
    public MucDoDongGop mucDoDongGop(String maBen) {

        if (maBen == null || !tongTheoBen.containsKey(maBen)) {
            throw new IllegalArgumentException("Không có bên đóng góp: " + maBen);
        }

        // MucDoDongGop.danhGia tự ném lỗi với NHA_NUOC nên không cần kiểm tra lại ở đây.
        return MucDoDongGop.danhGia(nhomCuaBen.get(maBen), tongTheoBen.get(maBen));
    }

    // Danh sách mã các bên đạt đúng mức đã chọn (bỏ qua nhà nước), xếp theo maBen tăng dần.
    public List<String> danhSachBenTheoMucDo(MucDoDongGop mucDo) {

        if (mucDo == null) {
            throw new IllegalArgumentException("Mức đóng góp không hợp lệ");
        }

        List<String> kq = new ArrayList<>();
        for (String maBen : tongTheoBen.keySet()) {
            if (nhomCuaBen.get(maBen) != NhomNguon.NHA_NUOC && mucDoDongGop(maBen) == mucDo) {
                kq.add(maBen);
            }
        }
        return kq;
    }

    // bên cáo nhất, trung bình, thấp
    // Mã bên góp nhiều nhất (không tính nhà nước). Bằng nhau thì lấy maBen nhỏ hơn.
    // Không có bên nào thì trả null.
    public String benCaoNhat() {
        String ketQua = null;
        // Duyệt theo maBen tăng dần và chỉ thay khi LỚN HƠN HẲN (>) nên khi bằng nhau
        // bên đứng trước (maBen nhỏ hơn) được giữ lại.
        for (Map.Entry<String, Long> e : tongTheoBen.entrySet()) {
            if (nhomCuaBen.get(e.getKey()) == NhomNguon.NHA_NUOC) {
                continue;
            }
            if (ketQua == null || e.getValue() > tongTheoBen.get(ketQua)) {
                ketQua = e.getKey();
            }
        }
        return ketQua;
    }

    // Mã bên góp ít nhất (không tính nhà nước). Bằng nhau thì lấy maBen nhỏ hơn.
    // Không có bên nào thì trả null.
    public String benThapNhat() {
        String ketQua = null;
        for (Map.Entry<String, Long> e : tongTheoBen.entrySet()) {
            if (nhomCuaBen.get(e.getKey()) == NhomNguon.NHA_NUOC) {
                continue;
            }
            if (ketQua == null || e.getValue() < tongTheoBen.get(ketQua)) {
                ketQua = e.getKey();
            }
        }
        return ketQua;
    }

    // Trung bình số tiền mỗi bên đã góp (không tính nhà nước). Không có bên nào thì trả 0.0.
    public double trungBinhMoiBen() {
        long tong = 0;
        int soBen = 0;
        for (Map.Entry<String, Long> e : tongTheoBen.entrySet()) {
            if (nhomCuaBen.get(e.getKey()) == NhomNguon.NHA_NUOC) {
                continue;
            }
            tong += e.getValue();
            soBen++;
        }
        return soBen == 0 ? 0.0 : (double) tong / soBen;
    }
}