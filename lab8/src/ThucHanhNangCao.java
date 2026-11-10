
import java.sql.*;
import java.util.Scanner;

public class ThucHanhNangCao {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        String url = "jdbc:sqlite:nhanvien.db";

        Connection conn = null;
        Statement stmt = null;

        try {

            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);

            System.out.println("Ket noi SQLite thanh cong!");

            stmt = conn.createStatement();

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS SanPham (" +
                "maSP INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenSP TEXT NOT NULL, " +
                "gia REAL, " +
                "soluong INTEGER)"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS NhanVien (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ten TEXT NOT NULL, " +
                "chuc_vu TEXT)"
            );

       
            boolean coLuong = false;

            ResultSet rs = stmt.executeQuery(
                "PRAGMA table_info(NhanVien)"
            );

            while (rs.next()) {
                if ("luong".equalsIgnoreCase(
                    rs.getString("name"))) {
                    coLuong = true;
                    break;
                }
            }

            rs.close();
            rs = null;

            if (!coLuong) {
                stmt.executeUpdate(
                    "ALTER TABLE NhanVien ADD COLUMN luong REAL"
                );
            }

            stmt.close();
            stmt = null;


            int chon = 0;

            do {
                System.out.println("\n========== MENU ==========");
                System.out.println("1. Transaction cap nhat san pham");
                System.out.println("2. Them nhan vien va luong");
                System.out.println("3. Thong ke luong nhan vien");
                System.out.println("4. Dem nhan vien theo chuc vu");
                System.out.println("5. Hien thi danh sach nhan vien");
                System.out.println("6. Thoat");
                System.out.println("==========================");

                System.out.print("Nhap lua chon: ");
                chon = Integer.parseInt(sc.nextLine());

                switch (chon) {

                    case 1:
                        transactionSanPham(conn);
                        break;

                    case 2:
                        themNhanVien(conn);
                        break;

                    case 3:
                        thongKeLuong(conn);
                        break;

                    case 4:
                        demNhanVienTheoChucVu(conn);
                        break;

                    case 5:
                        hienThiNhanVien(conn);
                        break;

                    case 6:
                        System.out.println("Thoat chuong trinh!");
                        break;

                    default:
                        System.out.println("Lua chon khong hop le!");
                }

            } while (chon != 6);

        } catch (ClassNotFoundException e) {

            System.out.println("Khong tim thay JDBC Driver!");
            e.printStackTrace();

        } catch (SQLException e) {

            System.out.println("Loi SQL!");
            e.printStackTrace();

        } catch (NumberFormatException e) {

            System.out.println("Du lieu nhap phai dung dinh dang so!");

        } finally {


            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (conn != null) {
                    conn.close();
                }

                System.out.println("Da dong ket noi!");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void transactionSanPham(Connection conn)
            throws SQLException {

        PreparedStatement pstmt = null;

        try {
            System.out.println("\n--- TRANSACTION ---");

            System.out.print("Nhap ma san pham thu 1: ");
            int ma1 = Integer.parseInt(sc.nextLine());

            System.out.print("Nhap gia moi: ");
            double gia1 = Double.parseDouble(sc.nextLine());

            System.out.print("Nhap so luong moi: ");
            int sl1 = Integer.parseInt(sc.nextLine());

            System.out.print("Nhap ma san pham thu 2: ");
            int ma2 = Integer.parseInt(sc.nextLine());

            System.out.print("Nhap gia moi: ");
            double gia2 = Double.parseDouble(sc.nextLine());

            System.out.print("Nhap so luong moi: ");
            int sl2 = Integer.parseInt(sc.nextLine());

            conn.setAutoCommit(false);

            String sql =
                "UPDATE SanPham SET gia = ?, soluong = ? " +
                "WHERE maSP = ?";

            pstmt = conn.prepareStatement(sql);


            pstmt.setDouble(1, gia1);
            pstmt.setInt(2, sl1);
            pstmt.setInt(3, ma1);

            int kq1 = pstmt.executeUpdate();

            if (kq1 != 1) {
                throw new SQLException(
                    "Khong tim thay san pham thu 1!"
                );
            }

            pstmt.setDouble(1, gia2);
            pstmt.setInt(2, sl2);
            pstmt.setInt(3, ma2);

            int kq2 = pstmt.executeUpdate();

            if (kq2 != 1) {
                throw new SQLException(
                    "Khong tim thay san pham thu 2!"
                );
            }

        
            conn.commit();

            System.out.println("Transaction thanh cong!");
            System.out.println("Da cap nhat 2 san pham!");
            
        } catch (SQLException e) {

  
            try {
                conn.rollback();
                System.out.println("Da rollback!");
                System.out.println(
                    "Du lieu cu duoc giu nguyen."
                );
            } catch (SQLException ex) {
                System.out.println("Loi rollback!");
                ex.printStackTrace();
            }

            System.out.println("Transaction that bai!");
            System.out.println(e.getMessage());

        } finally {

    
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                conn.setAutoCommit(true);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void themNhanVien(Connection conn)
            throws SQLException {

        PreparedStatement pstmt = null;

        try {
            System.out.println("\n--- THEM NHAN VIEN ---");

            System.out.print("Nhap ten nhan vien: ");
            String ten = sc.nextLine();

            System.out.print("Nhap chuc vu: ");
            String chucVu = sc.nextLine();

            System.out.print("Nhap luong: ");
            double luong = Double.parseDouble(sc.nextLine());

            String sql =
                "INSERT INTO NhanVien(ten, chuc_vu, luong) " +
                "VALUES(?, ?, ?)";

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ten);
            pstmt.setString(2, chucVu);
            pstmt.setDouble(3, luong);

            int kq = pstmt.executeUpdate();

            if (kq > 0) {
                System.out.println("Them nhan vien thanh cong!");
            }

        } catch (SQLException e) {

            System.out.println("Loi khi them nhan vien!");
            e.printStackTrace();

        } finally {

            if (pstmt != null) {
                pstmt.close();
            }
        }
    }


    public static void thongKeLuong(Connection conn)
            throws SQLException {

        Statement stmt = null;
        ResultSet rs = null;

        try {
            String sql =
                "SELECT SUM(luong) AS tongLuong, " +
                "AVG(luong) AS luongTB, " +
                "MIN(luong) AS luongThapNhat, " +
                "MAX(luong) AS luongCaoNhat " +
                "FROM NhanVien WHERE luong IS NOT NULL";

            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);

            if (rs.next()) {

                System.out.println("\n--- THONG KE LUONG ---");

                System.out.printf(
                    "Tong luong: %,.0f%n",
                    rs.getDouble("tongLuong")
                );

                System.out.printf(
                    "Luong trung binh: %,.0f%n",
                    rs.getDouble("luongTB")
                );

                System.out.printf(
                    "Luong thap nhat: %,.0f%n",
                    rs.getDouble("luongThapNhat")
                );

                System.out.printf(
                    "Luong cao nhat: %,.0f%n",
                    rs.getDouble("luongCaoNhat")
                );
            }

        } catch (SQLException e) {

            System.out.println("Loi thong ke!");
            e.printStackTrace();

        } finally {

            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }
        }
    }


    public static void demNhanVienTheoChucVu(Connection conn)
            throws SQLException {

        Statement stmt = null;
        ResultSet rs = null;

        try {
            String sql =
                "SELECT chuc_vu, COUNT(*) AS soLuong " +
                "FROM NhanVien " +
                "GROUP BY chuc_vu";

            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);

            System.out.println("\n--- SO NHAN VIEN THEO CHUC VU ---");

            boolean coDuLieu = false;

            while (rs.next()) {

                coDuLieu = true;

                System.out.println(
                    "Chuc vu: " + rs.getString("chuc_vu") +
                    " | So nhan vien: " +
                    rs.getInt("soLuong")
                );
            }

            if (!coDuLieu) {
                System.out.println("Chua co nhan vien!");
            }

        } catch (SQLException e) {

            System.out.println("Loi truy van!");
            e.printStackTrace();

        } finally {

            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public static void hienThiNhanVien(Connection conn)
            throws SQLException {

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();

            rs = stmt.executeQuery(
                "SELECT * FROM NhanVien"
            );

            System.out.println("\n--- DANH SACH NHAN VIEN ---");

            boolean coDuLieu = false;

            while (rs.next()) {

                coDuLieu = true;

                System.out.printf(
                    "ID: %d | Ten: %s | Chuc vu: %s | Luong: %,.0f%n",
                    rs.getInt("id"),
                    rs.getString("ten"),
                    rs.getString("chuc_vu"),
                    rs.getDouble("luong")
                );
            }

            if (!coDuLieu) {
                System.out.println("Danh sach nhan vien trong!");
            }

        } catch (SQLException e) {

            System.out.println("Loi hien thi!");
            e.printStackTrace();

        } finally {

            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
