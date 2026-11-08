package main;

import java.time.LocalDateTime;

public class BaoCao {

    private String maBaoCao;
    private String tenBaoCao;
    private LocalDateTime thoiGianLap;

    // Thien tai duoc lap bao cao
    private ThienTai thienTai;

    // Khu vuc duoc lap bao cao
    private KhuVuc khuVuc;

    // Khoi tao bao cao cho mot thien tai tai mot khu vuc cu the
    public BaoCao(String maBaoCao, String tenBaoCao,
                  ThienTai thienTai, KhuVuc khuVuc) {

        if (maBaoCao == null || maBaoCao.trim().isEmpty()
                || tenBaoCao == null || tenBaoCao.trim().isEmpty()
                || thienTai == null || khuVuc == null) {
            throw new IllegalArgumentException("Thong tin bao cao khong hop le");
        }

        this.maBaoCao = maBaoCao;
        this.tenBaoCao = tenBaoCao;
        this.thienTai = thienTai;
        this.khuVuc = khuVuc;
        this.thoiGianLap = LocalDateTime.now();
    }

    // Kiem tra thong tin bao cao hop le
    public boolean kiemTraHopLe() {
        return maBaoCao != null && !maBaoCao.trim().isEmpty()
                && tenBaoCao != null && !tenBaoCao.trim().isEmpty()
                && thoiGianLap != null
                && thienTai != null
                && khuVuc != null;
    }

    // Cac phuong thuc getter
    public String getMaBaoCao() {
        return maBaoCao;
    }

    public String getTenBaoCao() {
        return tenBaoCao;
    }

    public LocalDateTime getThoiGianLap() {
        return thoiGianLap;
    }

    public ThienTai getThienTai() {
        return thienTai;
    }

    public KhuVuc getKhuVuc() {
        return khuVuc;
    }
}