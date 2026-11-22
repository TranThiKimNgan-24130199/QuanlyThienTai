package main;
//người thực hiện: Nguyễn Thị Hoài Lan
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import trangthai.LoaiHoatDong;
import trangthai.NhomNguon;
import trangthai.TrangThaiKhacPhuc;

/*
 * ThongKeChi: thống kê phía CHI (dùng tiền vào việc gì) của MỘT thiên tai.
 *
 * Cách hoạt động giống ThongKeDongGop: nhận một KhacPhucThienTai, rồi trong constructor
 * gom sẵn số liệu MỘT LẦN. Các hàm thống kê bên dưới chỉ đọc từ số liệu đã gom.
 * Đây là bản chụp (snapshot) tại thời điểm tạo: chi thêm sau đó thì phải tạo ThongKeChi mới.
 *
 * Dữ liệu lấy từ:
 *  - các nguồn kinh phí (getDsNguonKinhPhi): biết mỗi nguồn huy động bao nhiêu, thuộc nhóm nào
 *  - các hoạt động khắc phục (getDsHoatDong): biết mỗi hoạt động chi từ nguồn nào, bao nhiêu
 *  - các khu vực của thiên tai: biết mỗi khu vực thuộc tỉnh/thành nào (để thống kê theo địa phương)
 */
public class ThongKeChi {

    // Tổng tiền huy động được (cộng mọi nguồn)
    private long tongHuyDong;

    // Tổng tiền đã chi (cộng mọi hoạt động)
    private long tongChi;

    // Đã chi từ từng nguồn, khóa là maNguon. Có đủ mọi nguồn, nguồn chưa chi thì là 0.
    private TreeMap<String, Long> chiTheoNguon = new TreeMap<>();

    // Còn lại của từng nguồn = huy động trừ đã chi, khóa là maNguon
    private TreeMap<String, Long> conLaiTheoNguon = new TreeMap<>();

    // Đã chi theo nhóm nguồn (nhà nước / cá nhân / tập thể / công ty). EnumMap: duyệt đúng thứ tự khai báo.
    private EnumMap<NhomNguon, Long> chiTheoNhomNguon = new EnumMap<>(NhomNguon.class);

    // Đã chi theo loại hoạt động (hỗ trợ hộ / sửa chữa hạ tầng / xây mới hạ tầng)
    private EnumMap<LoaiHoatDong, Long> chiTheoLoai = new EnumMap<>(LoaiHoatDong.class);

    // Đã chi theo khu vực (khóa là maKV), có đủ mọi khu vực của thiên tai
    private TreeMap<String, Long> chiTheoKhuVuc = new TreeMap<>();

    // Đã chi theo địa phương = tỉnh/thành phố (khóa là tinhThanhPho)
    private TreeMap<String, Long> chiTheoDiaPhuong = new TreeMap<>();

    // Số hoạt động theo trạng thái (đủ ba trạng thái)
    private EnumMap<TrangThaiKhacPhuc, Integer> demTheoTrangThai = new EnumMap<>(TrangThaiKhacPhuc.class);

    // Mã các hoạt động đã chi nhiều hơn kinh phí dự kiến, xếp theo mã tăng dần
    private List<String> hoatDongVuotDuKien = new ArrayList<>();

    /*
     * Gom số liệu từ một KhacPhucThienTai. Ném IllegalArgumentException nếu:
     *  - khắc phục null
     *  - có hoạt động chi từ một nguồn không còn trong danh sách nguồn
     *    (nếu bỏ qua thì tổng chi và tổng huy động sẽ không khớp nhau)
     *  - có hoạt động thuộc khu vực không có trong thiên tai
     */
    public ThongKeChi(KhacPhucThienTai khacPhuc) {

        if (khacPhuc == null) {
            throw new IllegalArgumentException("Khac phuc thien tai khong hop le");
        }

        TreeMap<String, NguonKinhPhi> dsNguon = khacPhuc.getDsNguonKinhPhi();
        TreeMap<String, HoatDongKhacPhuc> dsHoatDong = khacPhuc.getDsHoatDong();
        Map<String, KhuVuc> dsKhuVuc = khacPhuc.getThienTai().getDsKhuVuc();

        // Cho mọi khóa bắt đầu từ 0 để chỗ nào chưa có chi vẫn có giá trị 0,
        // người dùng không phải kiểm tra null.
        for (NguonKinhPhi n : dsNguon.values()) {
            chiTheoNguon.put(n.getMaNguon(), 0L);
            tongHuyDong += n.getTongSoTien();
        }
        for (NhomNguon n : NhomNguon.values()) {
            chiTheoNhomNguon.put(n, 0L);
        }
        for (LoaiHoatDong l : LoaiHoatDong.values()) {
            chiTheoLoai.put(l, 0L);
        }
        for (TrangThaiKhacPhuc t : TrangThaiKhacPhuc.values()) {
            demTheoTrangThai.put(t, 0);
        }
        for (KhuVuc kv : dsKhuVuc.values()) {
            chiTheoKhuVuc.put(kv.getMaKV(), 0L);
            chiTheoDiaPhuong.put(kv.getTinhThanhPho(), 0L);
        }

        // Duyệt từng hoạt động (theo mã tăng dần vì dsHoatDong là TreeMap)
        for (HoatDongKhacPhuc h : dsHoatDong.values()) {

            KhuVuc kv = dsKhuVuc.get(h.getMaKV());
            if (kv == null) {
                throw new IllegalArgumentException("Hoat dong " + h.getMaHoatDong()
                        + " thuoc khu vuc " + h.getMaKV() + " khong co trong thien tai");
            }

            long tongChiHoatDong = 0;

            // Mỗi hoạt động có thể chi từ nhiều nguồn
            for (Map.Entry<String, Long> e : h.getChiTheoNguon().entrySet()) {

                NguonKinhPhi nguon = dsNguon.get(e.getKey());
                if (nguon == null) {
                    throw new IllegalArgumentException("Hoat dong " + h.getMaHoatDong()
                            + " chi tu nguon " + e.getKey() + " khong con trong danh sach nguon");
                }

                long tien = e.getValue();
                tongChiHoatDong += tien;
                chiTheoNguon.put(nguon.getMaNguon(), chiTheoNguon.get(nguon.getMaNguon()) + tien);
                chiTheoNhomNguon.put(nguon.getNhomNguon(), chiTheoNhomNguon.get(nguon.getNhomNguon()) + tien);
            }

            tongChi += tongChiHoatDong;
            chiTheoLoai.put(h.getLoai(), chiTheoLoai.get(h.getLoai()) + tongChiHoatDong);
            chiTheoKhuVuc.put(kv.getMaKV(), chiTheoKhuVuc.get(kv.getMaKV()) + tongChiHoatDong);
            chiTheoDiaPhuong.put(kv.getTinhThanhPho(),
                    chiTheoDiaPhuong.get(kv.getTinhThanhPho()) + tongChiHoatDong);

            demTheoTrangThai.put(h.getTrangThai(), demTheoTrangThai.get(h.getTrangThai()) + 1);

            // Chi nhiều hơn dự kiến thì ghi lại (được phép, chỉ báo để biết)
            if (tongChiHoatDong > h.getKinhPhiDuKien()) {
                hoatDongVuotDuKien.add(h.getMaHoatDong());
            }
        }

        // Còn lại của từng nguồn = huy động trừ đã chi
        for (NguonKinhPhi n : dsNguon.values()) {
            conLaiTheoNguon.put(n.getMaNguon(), n.getTongSoTien() - chiTheoNguon.get(n.getMaNguon()));
        }
    }

    // tổng
    // Tổng tiền đã huy động được
    public long tongHuyDong() {
        return tongHuyDong;
    }

    // Tổng tiền đã chi
    public long tongChi() {
        return tongChi;
    }

    // Tiền còn lại = huy động trừ đã chi
    public long conLai() {
        return tongHuyDong - tongChi;
    }

    // Tỷ lệ phần trăm (0..100) đã chi trên tổng huy động. Chưa huy động đồng nào thì là 0.0 để tránh chia cho 0.
    public double tyLeDaChi() {
        // nhân 100.0 (số thực) để phép chia không bị cắt phần thập phân như chia số nguyên
        return tongHuyDong == 0 ? 0.0 : tongChi * 100.0 / tongHuyDong;
    }

    // kinh phí từ nguồn nào
    // Đã chi từ từng nguồn. Trả về bản sao để bên ngoài không sửa được số liệu bên trong.
    public TreeMap<String, Long> chiTheoNguon() {
        return new TreeMap<>(chiTheoNguon);
    }

    // Đã chi theo nhóm nguồn (đủ bốn nhóm): cho biết nhà nước chi bao nhiêu, đóng góp chi bao nhiêu
    public EnumMap<NhomNguon, Long> chiTheoNhomNguon() {
        return new EnumMap<>(chiTheoNhomNguon);
    }

    // Còn lại của từng nguồn (huy động trừ đã chi)
    public TreeMap<String, Long> conLaiTheoNguon() {
        return new TreeMap<>(conLaiTheoNguon);
    }

    // chi theo đối tượng
    // Đã chi theo loại hoạt động (đủ ba loại)
    public EnumMap<LoaiHoatDong, Long> chiTheoLoai() {
        return new EnumMap<>(chiTheoLoai);
    }

    // Đã chi theo khu vực (đủ mọi khu vực của thiên tai)
    public TreeMap<String, Long> chiTheoKhuVuc() {
        return new TreeMap<>(chiTheoKhuVuc);
    }

    // Đã chi theo địa phương (tỉnh/thành phố), gộp các khu vực cùng tỉnh
    public TreeMap<String, Long> chiTheoDiaPhuong() {
        return new TreeMap<>(chiTheoDiaPhuong);
    }

    // tiến độ
    // Số hoạt động theo trạng thái (đủ ba trạng thái)
    public EnumMap<TrangThaiKhacPhuc, Integer> demHoatDongTheoTrangThai() {
        return new EnumMap<>(demTheoTrangThai);
    }

    // Mã các hoạt động đã chi nhiều hơn kinh phí dự kiến, xếp theo mã tăng dần
    public List<String> danhSachHoatDongVuotDuKien() {
        return new ArrayList<>(hoatDongVuotDuKien);
    }
}