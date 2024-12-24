import redis

def get_redis_connection():
    """
    Buat koneksi ke Redis menggunakan konfigurasi yang telah ditentukan.
    """
    return redis.StrictRedis(
        host='10.77.244.11',  # Ganti dengan IP Memorystore Redis Anda
        port=6379,
        decode_responses=True
    )
