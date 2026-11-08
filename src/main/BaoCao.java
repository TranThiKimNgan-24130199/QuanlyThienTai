package main;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BaoCao {
    private String maBaoCao;
    private String tenBaoCao;
    private LocalDateTime thoiGianLap;
    private LocalDateTime tuNgay;
    private LocalDateTime denNgay;
    private List<ThienTai> dsThienTai = new ArrayList<>(); 

    public BaoCao(String maBaoCao, String tenBaoCao) {
        this.maBaoCao = maBaoCao;
        this.tenBaoCao = tenBaoCao;
    }

    public void taoBaoCao(List<ThienTai> nguon, LocalDateTime tuNgay, LocalDateTime denNgay) {
//        this.tuNgay = tuNgay;
//        this.denNgay = denNgay;
//        this.thoiGianLap = LocalDateTime.now();
//        dsThienTai.clear();
//        for (ThienTai t : nguon)
//            if (!t.getThoiGianBatDau().isBefore(tuNgay) && !t.getThoiGianBatDau().isAfter(denNgay))
//                dsThienTai.add(t);
    }

    public List<BanGhiThienTai> locTheoThienTai(String maTT) {
		return null;
//        List<BanGhiThienTai> kq = new ArrayList<>();
//        for (ThienTai t : dsThienTai)
//            if (t.getMaTT().equals(maTT)) kq.addAll(t.getDsBanGhi().values());
//        return kq;
    }

    public List<BanGhiThienTai> locTheoKhuVuc(String maKV) {
		return null;
//        List<BanGhiThienTai> kq = new ArrayList<>();
//        for (ThienTai t : dsThienTai)
//            for (BanGhiThienTai b : t.getDsBanGhi().values())
//                if (b.getKhuVuc().getMaKV().equals(maKV)) kq.add(b);
//        return kq;
    }

    public boolean kiemTraHopLe() {
    	return false;
    }
// getter
	public String getMaBaoCao() {
		return maBaoCao;
	}

	public String getTenBaoCao() {
		return tenBaoCao;
	}

	public LocalDateTime getThoiGianLap() {
		return thoiGianLap;
	}

	public LocalDateTime getTuNgay() {
		return tuNgay;
	}

	public LocalDateTime getDenNgay() {
		return denNgay;
	}

  
}

