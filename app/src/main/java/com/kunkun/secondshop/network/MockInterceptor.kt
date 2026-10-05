package com.kunkun.secondshop.network

import com.kunkun.secondshop.util.KunkunConstants
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

// Interceptor untuk mensimulasikan respons server dengan data tiruan
class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!KunkunConstants.USE_MOCK) {
            return chain.proceed(chain.request())
        }

        Thread.sleep(1000)

        val url = chain.request().url.toString()

        val responseJson = when {
            url.contains("data/categories.json") -> CATEGORIES_JSON
            url.contains("data/products.json")   -> PRODUCTS_JSON
            else -> "[]"
        }

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .body(responseJson.toResponseBody("application/json".toMediaType()))
            .addHeader("content-type", "application/json")
            .build()
    }

    companion object {

        // Data tiruan kategori
        private val CATEGORIES_JSON = """
            [
                {
                    "id": 1,
                    "name": "Atasan",
                    "description": "Kemeja, kaos, blouse, dan berbagai jenis atasan",
                    "products_count": 4
                },
                {
                    "id": 2,
                    "name": "Bawahan",
                    "description": "Celana, rok, dan berbagai jenis bawahan",
                    "products_count": 4
                },
                {
                    "id": 3,
                    "name": "Outer",
                    "description": "Jaket, blazer, hoodie, dan berbagai jenis outer",
                    "products_count": 4
                },
                {
                    "id": 4,
                    "name": "Aksesoris",
                    "description": "Topi, tas, ikat pinggang, dan aksesoris fashion",
                    "products_count": 4
                }
            ]
        """.trimIndent()

        // Data tiruan produk
        private val PRODUCTS_JSON = """
            [
                {
                    "id": 1,
                    "category_id": 1,
                    "name": "Kemeja Flannel H&M",
                    "description": "Kemeja flannel H&M warna merah kotak-kotak. Bahan tebal cotton-polyester blend yang hangat, cocok untuk gaya kasual.",
                    "price": 85000,
                    "img": "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"M": 3, "L": 2},
                    "brand": "H&M"
                },
                {
                    "id": 2,
                    "category_id": 1,
                    "name": "Kaos Polos Uniqlo",
                    "description": "Kaos polos Uniqlo warna putih, bahan Supima Cotton premium yang terasa lembut dan adem. Nyaman untuk daily wear.",
                    "price": 65000,
                    "img": "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"S": 2, "M": 3, "L": 2},
                    "brand": "Uniqlo"
                },
                {
                    "id": 3,
                    "category_id": 1,
                    "name": "Blouse Floral Zara",
                    "description": "Blouse Zara motif bunga-bunga cantik, bahan chiffon ringan yang jatuh sempurna. Cocok untuk tampilan feminin dan elegan.",
                    "price": 120000,
                    "img": "https://images.unsplash.com/photo-1554568218-0f1715e72254?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"XS": 2, "S": 3, "M": 2},
                    "brand": "Zara"
                },
                {
                    "id": 4,
                    "category_id": 1,
                    "name": "Polo Shirt Lacoste",
                    "description": "Polo shirt Lacoste original warna navy, bahan pique cotton yang adem dan breathable. Logo buaya bordir masih jelas.",
                    "price": 150000,
                    "img": "https://images.unsplash.com/photo-1626497764746-6dc36546b388?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"L": 3, "XL": 2},
                    "brand": "Lacoste"
                },
                {
                    "id": 5,
                    "category_id": 2,
                    "name": "Celana Jeans Levi's 501",
                    "description": "Celana jeans Levi's 501 original, warna biru medium wash yang ikonik. Bahan denim tebal 12oz berkualitas tinggi.",
                    "price": 200000,
                    "img": "https://images.unsplash.com/photo-1542272604-787c3835535d?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"30": 2, "32": 3},
                    "brand": "Levi's"
                },
                {
                    "id": 6,
                    "category_id": 2,
                    "name": "Celana Chino Zara",
                    "description": "Celana chino Zara warna krem, bahan cotton twill yang nyaman dan tidak mudah kusut. Potongan slim fit yang modern.",
                    "price": 110000,
                    "img": "https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"28": 2, "30": 3, "32": 2},
                    "brand": "Zara"
                },
                {
                    "id": 7,
                    "category_id": 2,
                    "name": "Rok Midi Floral Mango",
                    "description": "Rok midi motif bunga-bunga cantik dari Mango, bahan rayon yang jatuh dan nyaman dipakai seharian. Panjang hingga bawah lutut.",
                    "price": 75000,
                    "img": "https://images.unsplash.com/photo-1583496661160-fb5218afa9a0?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"S": 3, "M": 2},
                    "brand": "Mango"
                },
                {
                    "id": 8,
                    "category_id": 2,
                    "name": "Celana Jogger Nike",
                    "description": "Celana jogger Nike warna abu-abu, bahan fleece tebal yang hangat dan sangat nyaman. Cocok untuk olahraga ringan maupun bersantai.",
                    "price": 130000,
                    "img": "https://images.unsplash.com/photo-1556906781-9a412961a28d?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"M": 3, "L": 2, "XL": 2},
                    "brand": "Nike"
                },
                {
                    "id": 9,
                    "category_id": 3,
                    "name": "Jaket Denim Levi's",
                    "description": "Jaket denim Levi's original warna biru tua, bahan denim tebal yang sangat berkualitas. Tombol logam masih sempurna dan kuat.",
                    "price": 250000,
                    "img": "https://images.unsplash.com/photo-1551537482-f2075a1d41f2?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"M": 3, "L": 2},
                    "brand": "Levi's"
                },
                {
                    "id": 10,
                    "category_id": 3,
                    "name": "Blazer Formal H&M",
                    "description": "Blazer formal H&M warna hitam, potongan slim fit yang elegan dan modern. Bahan polyester-viscose blend yang rapi dan tidak mudah kusut.",
                    "price": 175000,
                    "img": "https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"M": 2, "L": 3},
                    "brand": "H&M"
                },
                {
                    "id": 11,
                    "category_id": 3,
                    "name": "Hoodie Champion",
                    "description": "Hoodie Champion warna hijau army, bahan fleece cotton tebal yang hangat dan nyaman. Logo Champion bordiran masih sangat jelas.",
                    "price": 160000,
                    "img": "https://images.unsplash.com/photo-1556821840-3a63f15732ce?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"L": 1, "XL": 1},
                    "brand": "Champion"
                },
                {
                    "id": 12,
                    "category_id": 3,
                    "name": "Bomber Jacket Uniqlo",
                    "description": "Bomber jacket Uniqlo warna hitam, bahan nylon ringan namun hangat dengan lapisan dalam yang nyaman. Ritsleting YKK masih sangat mulus.",
                    "price": 220000,
                    "img": "https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"M": 3, "L": 2, "XL": 2},
                    "brand": "Uniqlo"
                },
                {
                    "id": 13,
                    "category_id": 4,
                    "name": "Topi Baseball New Era",
                    "description": "Topi baseball New Era original, warna hitam dengan logo NY Yankees yang ikonik. Bahan cotton twill yang kuat dan nyaman dipakai seharian.",
                    "price": 95000,
                    "img": "https://images.unsplash.com/photo-1588850561407-ed78c282e89b?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"One Size": 1},
                    "brand": "New Era"
                },
                {
                    "id": 14,
                    "category_id": 4,
                    "name": "Tas Selempang Zara",
                    "description": "Tas selempang Zara warna cokelat, bahan faux leather berkualitas dengan jahitan rapi. Ukuran sedang — cukup untuk dompet, HP, dan barang kecil lainnya.",
                    "price": 145000,
                    "img": "https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"One Size": 1},
                    "brand": "Zara"
                },
                {
                    "id": 15,
                    "category_id": 4,
                    "name": "Ikat Pinggang Kulit H&M",
                    "description": "Ikat pinggang genuine leather H&M warna hitam dengan gesper logam silver yang elegan. Lubang gesper masih sempurna, tidak ada retak atau mengelupas.",
                    "price": 70000,
                    "img": "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"One Size": 3},
                    "brand": "H&M"
                },
                {
                    "id": 16,
                    "category_id": 4,
                    "name": "Syal Kasmir Uniqlo",
                    "description": "Syal premium Uniqlo bahan wool-cashmere blend yang sangat lembut dan hangat di kulit. Warna abu-abu netral yang mudah dipadukan dengan outfit apapun.",
                    "price": 180000,
                    "img": "https://images.unsplash.com/photo-1520903920243-00d872a2d1c9?w=400&h=400&fit=crop&q=80",
                    "size_stocks": {"One Size": 2},
                    "brand": "Uniqlo"
                }
            ]
        """.trimIndent()
    }
}
