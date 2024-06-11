Çiftlik Otomasyon Sistemi
Proje Hakkında
Çiftlik Otomasyon Sistemi, bir çiftlikteki hayvanların, yem stoklarının ve diğer çiftlik kaynaklarının yönetimini kolaylaştırmak için geliştirilmiş kapsamlı bir yazılımdır. Bu sistem, hayvanların takibinden yem siparişine kadar birçok işlevi otomatikleştirir ve çiftlik operasyonlarının verimliliğini artırır.

Ana Özellikler
1. Kullanıcı Giriş ve Kayıt Sistemi
Giriş Ekranı: Kullanıcıların sisteme giriş yapabilmesi için bir kullanıcı adı ve şifre ile oturum açma ekranı.
Kayıt Olma: Yeni kullanıcıların sisteme kayıt olabilmesi için gerekli form ve doğrulama işlemleri.
2. Hayvan Yönetimi
Hayvan Ekle: Yeni hayvanların sisteme eklenmesi.
Hayvan Sil: Mevcut hayvanların sistemden silinmesi.
Hayvan Düzenle: Hayvan bilgilerini güncelleme.
Hayvan Ara: Hayvanları belirli kriterlere göre arama.
3. Irk Yönetimi
Irk Ekle: Yeni hayvan ırklarının sisteme eklenmesi.
Irk Sil: Mevcut hayvan ırklarının sistemden silinmesi.
Irk Düzenle: Hayvan ırkı bilgilerini güncelleme.
4. Hastalık Yönetimi
Hayvan Hastalık Takibi: Hayvanların hastalık durumlarını izleme ve hasta olma ihtimallerini değerlendirme.
5. Stok Takip Sistemi
Yem Stok Güncelleme: Yem stoklarının güncellenmesi ve takip edilmesi.
Yem Azaldığında Otomatik Sipariş: Yem stoku belirli bir seviyenin altına düştüğünde otomatik olarak sipariş oluşturma ve bilgilendirme.
6. Sipariş ve Bildirim Sistemi
Otomatik Sipariş Oluşturma: Yem stoku azaldığında otomatik olarak sipariş oluşturulması.
E-posta Bildirimi: Oluşturulan siparişler için ilgili şirkete e-posta gönderimi.
Teknik Detaylar
Kullanılan Teknolojiler
Java: Uygulama geliştirme dili.
Swing: Kullanıcı arayüzü tasarımı için.
JDBC: Veritabanı bağlantısı ve yönetimi için.
PostgreSQL: Veritabanı yönetim sistemi.
JavaMail API: E-posta gönderimi için.
Veritabanı Yapısı
Kullanıcılar Tablosu: Kullanıcı bilgilerini (kullanıcı adı, şifre, e-posta) tutar.
Hayvanlar Tablosu: Hayvan bilgilerini (ID, isim, ırk, yaş, hastalık durumu) tutar.
Irklar Tablosu: Hayvan ırk bilgilerini tutar.
Yem Stok Tablosu: Yem stok bilgilerini (yem türü, miktar) tutar.
Şirketler Tablosu: Yem tedarikçi şirket bilgilerini (isim, e-posta) tutar.
Örnek Kullanım Senaryoları
Kullanıcı Girişi ve Kayıt Olma
Kullanıcı, giriş ekranından kullanıcı adı ve şifresi ile sisteme giriş yapar.
Yeni kullanıcı, kayıt olma formunu doldurarak sisteme kayıt olur.
Hayvan Yönetimi
Kullanıcı, yeni bir hayvan eklemek için "Hayvan Ekle" formunu doldurur.
Kullanıcı, mevcut bir hayvanın bilgilerini güncellemek için "Hayvan Düzenle" ekranını kullanır.
Kullanıcı, belirli bir hayvanı aramak için "Hayvan Ara" fonksiyonunu kullanır.
Yem Stok Takibi ve Sipariş
Kullanıcı, mevcut yem stoklarını görüntüleyebilir ve güncelleyebilir.
Yem stoku belirli bir seviyenin altına düştüğünde, sistem otomatik olarak ilgili şirkete e-posta göndererek yeni sipariş oluşturur.
Kullanıcı Arayüzü
Giriş Ekranı: Kullanıcı adı ve şifre ile giriş yapılabilen basit bir ekran.
Ana Menü: Kullanıcıların hayvan yönetimi, yem stok takibi ve diğer işlemleri yapabileceği ana ekran.
Hayvan Yönetim Ekranı: Hayvan ekleme, silme, düzenleme ve arama işlemleri için.
Yem Stok Ekranı: Yem stoklarının görüntülenmesi ve güncellenmesi için.
Sipariş Ekranı: Otomatik sipariş oluşturma ve e-posta bildirimleri için.
Güvenlik
Şifre Hashleme: Kullanıcı şifreleri güvenlik için hashlenir ve veritabanında öyle saklanır.
Güvenli E-posta Gönderimi: E-posta gönderimi için güvenli bağlantı ve kimlik doğrulama kullanılır.
Sonuç
Bu çiftlik otomasyon sistemi, çiftliklerin operasyonlarını daha verimli ve etkili bir şekilde yönetmelerine yardımcı olur. Hayvan yönetimi, yem stoku takibi ve otomatik sipariş gibi özellikler, çiftlik sahiplerinin işlerini kolaylaştırır ve zaman kazandırır.
