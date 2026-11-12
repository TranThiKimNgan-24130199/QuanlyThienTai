
package main;

import java.util.ArrayList;
import trangthai.QuanHeChuHo;

public class HoDan {

    private String maHo;
    private String diaChi;
    private NguoiDan chuHo;

    // Khu vuc ma ho dan dang sinh song
    private KhuVuc khuVuc;

    // Danh sach thanh vien, bao gom ca chu ho
    private ArrayList<NguoiDan> dsThanhVien = new ArrayList<>();

    public HoDan(String maHo, String diaChi, NguoiDan chuHo) {

        // Kiem tra ma ho
        if (maHo == null || maHo.trim().isEmpty()) {
            throw new IllegalArgumentException("Ma ho khong hop le");
        }

        // Kiem tra dia chi
        if (diaChi == null || diaChi.trim().isEmpty()) {
            throw new IllegalArgumentException("Dia chi khong hop le");
        }

        // Kiem tra chu ho
        if (chuHo == null) {
            throw new IllegalArgumentException("Chu ho khong hop le");
        }

        // Nguoi duoc chon lam chu ho phai co quan he CHU_HO
        if (chuHo.getQuanHeChuHo() != QuanHeChuHo.CHU_HO) {
            throw new IllegalArgumentException(
                "Nguoi duoc chon lam chu ho phai co quan he CHU_HO"
            );
        }

        this.maHo = maHo;
        this.diaChi = diaChi;
        this.chuHo = chuHo;

        // Chu ho cung la mot thanh vien trong ho
        dsThanhVien.add(chuHo);

        // Khu vuc duoc gan khi ho dan duoc them vao khu vuc
        this.khuVuc = null;
    }

    // Cap nhat dia chi ho dan
    public void capNhatDiaChi(String diaChi) {

        if (diaChi == null || diaChi.trim().isEmpty()) {
            throw new IllegalArgumentException("Dia chi khong hop le");
        }

        this.diaChi = diaChi;
    }

    // Them thanh vien vao ho
    public boolean themThanhVien(NguoiDan nd) {

        if (nd == null) {
            return false;
        }

        String cccd = nd.getCCCD();

        // Khong chap nhan CCCD rong
        if (cccd == null || cccd.trim().isEmpty()) {
            return false;
        }

        // Khong cho phep them nguoi co quan he CHU_HO
        if (nd.getQuanHeChuHo() == QuanHeChuHo.CHU_HO) {
            return false;
        }

        // Khong cho phep trung CCCD trong cung ho
        if (timThanhVien(cccd) != null) {
            return false;
        }
     // Khong cho phep thanh vien co quan he khong hop le
        if (nd.getQuanHeChuHo() == null
                || nd.getQuanHeChuHo() == QuanHeChuHo.CHU_HO) {
            return false;
        }

        return dsThanhVien.add(nd);
    }

    // Xoa thanh vien dua tren CCCD
    public boolean xoaThanhVien(String cccd) {

        if (cccd == null || cccd.trim().isEmpty()) {
            return false;
        }

        NguoiDan nd = timThanhVien(cccd);

        // Khong tim thay thanh vien
        if (nd == null) {
            return false;
        }

        // Khong cho phep xoa chu ho
        if (nd == chuHo) {
            return false;
        }

        return dsThanhVien.remove(nd);
    }

    // Tim thanh vien bang CCCD
    public NguoiDan timThanhVien(String cccd) {

        if (cccd == null || cccd.trim().isEmpty()) {
            return null;
        }

        for (NguoiDan nd : dsThanhVien) {
            if (cccd.equals(nd.getCCCD())) {
                return nd;
            }
        }

        return null;
    }

    // Dem so thanh vien trong ho
    public int demSoThanhVien() {
        return dsThanhVien.size();
    }

    // Gan ho dan voi khu vuc
    void ganKhuVuc(KhuVuc khuVuc) {
        this.khuVuc = khuVuc;
    }

    // Getter
    public String getMaHo() {
        return maHo;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public NguoiDan getChuHo() {
        return chuHo;
    }

    public KhuVuc getKhuVuc() {
        return khuVuc;
    }

    public ArrayList<NguoiDan> getDsThanhVien() {
        // Tra ve ban sao danh sach thanh vien
        return new ArrayList<>(dsThanhVien);
    }
}
