# Gunakan image Python resmi
FROM python:3.9-slim

# Set working directory
WORKDIR /app

# Salin file aplikasi dan model
COPY app.py mobilenet.h5 requirements.txt ./

# Install dependensi
RUN pip install --no-cache-dir -r requirements.txt

# Expose port (harus tetap 8080 untuk Cloud Run)
EXPOSE 8080

# Jalankan aplikasi
CMD ["python", "app.py"]
