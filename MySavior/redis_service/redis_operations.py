from redis_service.redis_connection import get_redis_connection

# Operasi Redis: Simpan data
def set_status(user_id, status, ttl=3600):
    redis_client = get_redis_connection()
    redis_client.set(f"status:{user_id}", status, ex=ttl)

# Operasi Redis: Ambil data
def get_status(user_id):
    redis_client = get_redis_connection()
    return redis_client.get(f"status:{user_id}")
