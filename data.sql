-- Tabel Plants (Tetap dipertahankan)
CREATE TABLE IF NOT EXISTS plants (
                                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nama VARCHAR(100) NOT NULL,
    path_gambar VARCHAR(255) NOT NULL,
    deskripsi TEXT NOT NULL,
    manfaat TEXT NOT NULL,
    efek_samping TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
    );

-- Tabel Pastries (Baru)
CREATE TABLE IF NOT EXISTS pastries (
                                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    judul VARCHAR(255) NOT NULL,
    gambar INTEGER NOT NULL, -- Menyimpan ID R.drawable
    deskripsi TEXT NOT NULL,
    bahan_utama TEXT NOT NULL,
    info_alergen TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

