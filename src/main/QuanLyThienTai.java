
package main;

import java.util.HashMap;
import java.util.TreeMap;

public class QuanLyThienTai {

    // Quan ly cac su kien thien tai, su dung ma thien tai lam khoa
    private TreeMap<String, ThienTai> dsThienTai = new TreeMap<>();

    // Quan ly cac khu vuc, su dung ma khu vuc lam khoa
    private HashMap<String, KhuVuc> dsKhuVuc = new HashMap<>();

    // Khong luu danh muc ho dan rieng vi moi KhuVuc da quan ly cac ho dan truc thuoc.
    // Khi can tim kiem ho dan tren toan he thong, duyet qua danh sach cac khu vuc.
    
    //quản lý thiên tai
    // Them mot su kien thien tai vao he thong
    public boolean themThienTai(ThienTai thienTai) {

        if (thienTai == null)
            return false;

        String maTT = thienTai.getMaTT();

        // Kiem tra ma thien tai khong duoc rong va khong duoc trung
        if (maTT == null || maTT.trim().isEmpty())
            return false;

        if (dsThienTai.containsKey(maTT))
            return false;

        dsThienTai.put(maTT, thienTai);
        return true;
    }

    // Xoa mot su kien thien tai khoi he thong bang ma thien tai
    public boolean xoaThienTai(String maTT) {

        if (maTT == null || maTT.trim().isEmpty())
            return false;

        return dsThienTai.remove(maTT) != null;
    }

    // Tim kiem su kien thien tai bang ma thien tai
    public ThienTai timThienTai(String maTT) {

        if (maTT == null || maTT.trim().isEmpty())
            return null;

        return dsThienTai.get(maTT);
    }

    // Dem tong so su kien thien tai dang duoc quan ly
    public int demSoThienTai() {
        return dsThienTai.size();
    }


    // ==================== QUAN LY KHU VUC ====================

    // Them mot khu vuc vao he thong
    public boolean themKhuVuc(KhuVuc khuVuc) {

        if (khuVuc == null)
            return false;

        String maKV = khuVuc.getMaKV();

        // Kiem tra ma khu vuc khong duoc rong va khong duoc trung
        if (maKV == null || maKV.trim().isEmpty())
            return false;

        if (dsKhuVuc.containsKey(maKV))
            return false;

        dsKhuVuc.put(maKV, khuVuc);
        return true;
    }

    // Xoa khu vuc khoi he thong
    public boolean xoaKhuVuc(String maKV) {

        if (maKV == null || maKV.trim().isEmpty())
            return false;

        KhuVuc khuVuc = dsKhuVuc.get(maKV);

        if (khuVuc == null)
            return false;

        // Khong xoa khu vuc khi van con ho dan hoac co so vat chat
        if (khuVuc.demSoHoDan() > 0 || khuVuc.demSoCoSoVatChat() > 0) {
            return false;
        }

        dsKhuVuc.remove(maKV);
        return true;
    }

    // Tim kiem khu vuc bang ma khu vuc
    public KhuVuc timKhuVuc(String maKV) {

        if (maKV == null || maKV.trim().isEmpty())
            return null;

        return dsKhuVuc.get(maKV);
    }

    // Dem tong so khu vuc dang duoc quan ly
    public int demSoKhuVuc() {
        return dsKhuVuc.size();
    }


    // ==================== QUAN LY HO DAN ====================

    // Them ho dan vao khu vuc tuong ung
    public boolean themHoDan(String maKV, HoDan hoDan) {

        if (maKV == null || maKV.trim().isEmpty() || hoDan == null) {
            return false;
        }

        String maHo = hoDan.getMaHo();

        if (maHo == null || maHo.trim().isEmpty())
            return false;

        // Kiem tra ma ho da ton tai trong bat ky khu vuc nao cua he thong chua
        if (timHoDan(maHo) != null)
            return false;

        // Tim khu vuc ma ho dan truc thuoc
        KhuVuc khuVuc = timKhuVuc(maKV);

        if (khuVuc == null)
            return false;

        // Kiem tra CCCD cua tung thanh vien trong ho dan
        // Dam bao moi CCCD chi xuat hien mot lan trong toan he thong
        for (NguoiDan nd : hoDan.getDsThanhVien()) {

            if (nd == null || kiemTraCCCD(nd.getCCCD())) {
                return false;
            }
        }

        // Them ho dan vao khu vuc
        // KhuVuc se tu quan ly va luu tru ho dan nay
        return khuVuc.themHoDan(hoDan);
    }

    // Xoa ho dan khoi khu vuc ma ho dang truc thuoc
    public boolean xoaHoDan(String maHo) {

        if (maHo == null || maHo.trim().isEmpty())
            return false;

        // Tim ho dan tren toan he thong
        HoDan hoDan = timHoDan(maHo);

        if (hoDan == null)
            return false;

        // Xac dinh khu vuc dang quan ly ho dan
        KhuVuc khuVuc = hoDan.getKhuVuc();

        if (khuVuc == null)
            return false;

        // Xoa ho dan khoi khu vuc
        // KhuVuc se dong thoi huy lien ket giua ho dan va khu vuc
        return khuVuc.xoaHoDan(maHo);
    }

    // Tim kiem ho dan tren toan he thong bang ma ho
    public HoDan timHoDan(String maHo) {

        if (maHo == null || maHo.trim().isEmpty())
            return null;

        // Duyet qua cac khu vuc de tim ho dan
        for (KhuVuc khuVuc : dsKhuVuc.values()) {

            HoDan hoDan = khuVuc.timHoDan(maHo);

            if (hoDan != null)
                return hoDan;
        }

        return null;
    }

    // Dem tong so ho dan trong tat ca cac khu vuc
    public int demSoHoDan() {

        int tongSoHoDan = 0;

        for (KhuVuc khuVuc : dsKhuVuc.values()) {
            tongSoHoDan += khuVuc.demSoHoDan();
        }

        return tongSoHoDan;
    }

    // Kiem tra CCCD da ton tai trong toan he thong hay chua
    public boolean kiemTraCCCD(String cccd) {

        if (cccd == null || cccd.trim().isEmpty())
            return false;

        // Duyet qua cac khu vuc dang duoc quan ly
        for (KhuVuc khuVuc : dsKhuVuc.values()) {

            // Duyet qua cac ho dan trong tung khu vuc
            for (HoDan hoDan : khuVuc.getDsHoDan().values()) {

                // Kiem tra CCCD cua tung thanh vien trong ho
                for (NguoiDan nd : hoDan.getDsThanhVien()) {

                    if (cccd.equals(nd.getCCCD()))
                        return true;
                }
            }
        }

        return false;
    }
    //quản lý cơ sở vật chất
    // Kiem tra ma CSVC da ton tai trong toan he thong hay chua
    public boolean kiemTraMaCSVC(String maCSVC) {

        if (maCSVC == null || maCSVC.trim().isEmpty())
            return false;

        // Duyet qua tat ca khu vuc de kiem tra ma co so vat chat
        for (KhuVuc khuVuc : dsKhuVuc.values()) {

            if (khuVuc.timCoSoVatChat(maCSVC) != null) {
                return true;
            }
        }

        return false;
    }

    // Them co so vat chat vao khu vuc tuong ung
    public boolean themCoSoVatChat(String maKV, ThietHaiCoSoVatChat_NN coSo) {

        if (maKV == null || maKV.trim().isEmpty() || coSo == null) {
            return false;
        }

        KhuVuc khuVuc = timKhuVuc(maKV);

        if (khuVuc == null)
            return false;

        String maCSVC = coSo.getMaCSVC();

        if (maCSVC == null || maCSVC.trim().isEmpty())
            return false;

        // Dam bao ma CSVC duy nhat tren toan he thong
        if (kiemTraMaCSVC(maCSVC))
            return false;

        // Kiem tra tinh hop le cua doi tuong truoc khi them
        if (!coSo.kiemTraHopLe())
            return false;

        // Chuyen viec luu tru co so vat chat cho khu vuc quan ly
        return khuVuc.themCoSoVatChat(coSo);
    }

    // Xoa co so vat chat khoi khu vuc bang ma khu vuc va ma co so vat chat
    public boolean xoaCoSoVatChat(String maKV, String maCSVC) {

        if (maKV == null || maKV.trim().isEmpty()
                || maCSVC == null || maCSVC.trim().isEmpty()) {
            return false;
        }

        KhuVuc khuVuc = timKhuVuc(maKV);

        if (khuVuc == null)
            return false;

        // Khu vuc thuc hien xoa co so vat chat khoi danh sach quan ly
        return khuVuc.xoaCoSoVatChat(maCSVC);
    }
    //getter
    // Tra ve ban sao danh sach thien tai de tranh thay doi truc tiep danh sach noi bo
    public TreeMap<String, ThienTai> getDsThienTai() {
        return new TreeMap<>(dsThienTai);
    }

    // Tra ve ban sao danh sach khu vuc de tranh thay doi truc tiep danh sach noi bo
    public HashMap<String, KhuVuc> getDsKhuVuc() {
        return new HashMap<>(dsKhuVuc);
    }

    // Tra ve danh sach ho dan cua toan he thong
    // Danh sach duoc tong hop tu cac khu vuc, khong luu tru them mot ban rieng
    public HashMap<String, HoDan> getDsHoDan() {

        HashMap<String, HoDan> dsHoDan = new HashMap<>();

        // Tong hop ho dan tu tat ca cac khu vuc
        for (KhuVuc khuVuc : dsKhuVuc.values()) {
            dsHoDan.putAll(khuVuc.getDsHoDan());
        }

        return dsHoDan;
    }

}
