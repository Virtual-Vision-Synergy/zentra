import axios, {type AxiosInstance} from 'axios';

const client: AxiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
    headers: {'Content-Type': 'application/json'},
});

type Headers = Record<string, string>;

client.interceptors.request.use((config) => {
    const token = localStorage.getItem('qcm_token');
    if (token) (config.headers as Headers).Authorization = `Bearer ${token}`;
    return config;
});

export async function get<T = any>(path: string, headers: Headers = {}): Promise<T> {
    const res = await client.get(path, {headers});
    return res.data;
}

export async function post<T = any>(path: string, data?: any, headers: Headers = {}): Promise<T> {
    const res = await client.post(path, data, {headers});
    return res.data;
}

export async function put<T = any>(path: string, data?: any, headers: Headers = {}): Promise<T> {
    const res = await client.put(path, data, {headers});
    return res.data;
}

export async function patch<T = any>(path: string, data?: any, headers: Headers = {}): Promise<T> {
    const res = await client.patch(path, data, {headers});
    return res.data;
}

export async function del<T = any>(path: string, headers: Headers = {}): Promise<T> {
    const res = await client.delete(path, {headers});
    return res.data;
}

export async function downloadFile(filePath: string, fileName: string): Promise<void> {
    try {
        const token = localStorage.getItem('qcm_token');
        const apiUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

        const response = await fetch(`${apiUrl}/files/${filePath}`, {
            method: 'GET',
            headers: {
                'Authorization': token ? `Bearer ${token}` : '',
            },
        });

        if (!response.ok) {
            throw new Error('Erreur lors du téléchargement du fichier');
        }

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = fileName;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
    } catch (err) {
        console.error('Erreur lors du téléchargement:', err);
        throw err;
    }
}

