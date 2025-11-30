import React, { useState, useEffect } from 'react';
import axios from 'axios';

/**
 * Composant de test pour diagnostiquer le chargement des employés
 * Accès: http://localhost:5173/test-employees
 */
const TestEmployeesPage: React.FC = () => {
  const [employees, setEmployees] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const [rawResponse, setRawResponse] = useState<string>('');

  useEffect(() => {
    testAPI();
  }, []);

  const testAPI = async () => {
    setLoading(true);
    setError('');

    const API_URL = 'http://localhost:8080/api/hr/employees';

    console.log('🔍 Test API - URL:', API_URL);

    try {
      const response = await axios.get(API_URL);

      console.log('✅ Réponse reçue:', response);
      console.log('📦 Data:', response.data);
      console.log('📊 Nombre d\'employés:', response.data.length);

      setEmployees(response.data);
      setRawResponse(JSON.stringify(response.data, null, 2));

      if (response.data.length === 0) {
        setError('⚠️ La liste est vide - Aucun employé dans la base de données');
      }
    } catch (err: any) {
      console.error('❌ Erreur:', err);
      console.error('Response:', err.response);

      const errorMsg = err.response?.data?.message || err.message || 'Erreur inconnue';
      setError(`❌ Erreur: ${errorMsg}`);
      setRawResponse(JSON.stringify(err.response?.data || err.message, null, 2));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: '40px', maxWidth: '1200px', margin: '0 auto', fontFamily: 'Arial, sans-serif' }}>
      <h1 style={{ color: '#2c3e50', marginBottom: '30px' }}>
        🧪 Test de Chargement des Employés
      </h1>

      <div style={{
        background: '#f8f9fa',
        padding: '20px',
        borderRadius: '8px',
        marginBottom: '30px',
        border: '1px solid #dee2e6'
      }}>
        <h3 style={{ marginTop: 0 }}>📍 Configuration</h3>
        <p><strong>URL API:</strong> <code>http://localhost:8080/api/hr/employees</code></p>
        <p><strong>Méthode:</strong> GET</p>
        <button
          onClick={testAPI}
          style={{
            background: '#007bff',
            color: 'white',
            border: 'none',
            padding: '10px 20px',
            borderRadius: '5px',
            cursor: 'pointer',
            fontSize: '14px',
            fontWeight: 'bold'
          }}
        >
          🔄 Recharger
        </button>
      </div>

      {loading && (
        <div style={{
          background: '#fff3cd',
          padding: '20px',
          borderRadius: '8px',
          border: '1px solid #ffc107',
          marginBottom: '20px'
        }}>
          <h3 style={{ marginTop: 0 }}>⏳ Chargement en cours...</h3>
        </div>
      )}

      {error && (
        <div style={{
          background: '#f8d7da',
          padding: '20px',
          borderRadius: '8px',
          border: '1px solid #dc3545',
          marginBottom: '20px',
          color: '#721c24'
        }}>
          <h3 style={{ marginTop: 0 }}>{error}</h3>
          <p style={{ margin: 0 }}>
            Vérifiez que le backend est démarré et que l'endpoint existe.
          </p>
        </div>
      )}

      {!loading && !error && employees.length > 0 && (
        <div style={{
          background: '#d4edda',
          padding: '20px',
          borderRadius: '8px',
          border: '1px solid #28a745',
          marginBottom: '20px',
          color: '#155724'
        }}>
          <h3 style={{ marginTop: 0 }}>
            ✅ Succès ! {employees.length} employé(s) chargé(s)
          </h3>
        </div>
      )}

      {!loading && (
        <>
          <div style={{ marginBottom: '30px' }}>
            <h2 style={{ color: '#2c3e50' }}>
              📊 Résultats ({employees.length})
            </h2>

            {employees.length > 0 ? (
              <table style={{
                width: '100%',
                borderCollapse: 'collapse',
                background: 'white',
                boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
              }}>
                <thead>
                  <tr style={{ background: '#007bff', color: 'white' }}>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #dee2e6' }}>ID</th>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #dee2e6' }}>Prénom</th>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #dee2e6' }}>Nom</th>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #dee2e6' }}>Poste</th>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #dee2e6' }}>Salaire</th>
                  </tr>
                </thead>
                <tbody>
                  {employees.map((emp, index) => (
                    <tr key={emp.id || index} style={{
                      background: index % 2 === 0 ? 'white' : '#f8f9fa'
                    }}>
                      <td style={{ padding: '12px', border: '1px solid #dee2e6' }}>{emp.id}</td>
                      <td style={{ padding: '12px', border: '1px solid #dee2e6' }}>{emp.firstName}</td>
                      <td style={{ padding: '12px', border: '1px solid #dee2e6' }}>{emp.lastName}</td>
                      <td style={{ padding: '12px', border: '1px solid #dee2e6' }}>{emp.position || 'N/A'}</td>
                      <td style={{ padding: '12px', border: '1px solid #dee2e6' }}>
                        {emp.salary ? emp.salary.toLocaleString() + ' Ar' : 'N/A'}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <p style={{ color: '#6c757d', fontStyle: 'italic' }}>
                Aucun employé à afficher
              </p>
            )}
          </div>

          <div>
            <h2 style={{ color: '#2c3e50' }}>📄 Réponse Brute (JSON)</h2>
            <pre style={{
              background: '#f8f9fa',
              padding: '20px',
              borderRadius: '8px',
              overflow: 'auto',
              maxHeight: '400px',
              border: '1px solid #dee2e6',
              fontSize: '12px'
            }}>
              {rawResponse || 'Aucune donnée'}
            </pre>
          </div>

          <div style={{ marginTop: '30px', padding: '20px', background: '#e7f3ff', borderRadius: '8px', border: '1px solid #b3d9ff' }}>
            <h3 style={{ marginTop: 0, color: '#004085' }}>💡 Instructions de Diagnostic</h3>
            <ol style={{ color: '#004085', lineHeight: '1.8' }}>
              <li>
                <strong>Si erreur 404/500 :</strong> Le backend n'est pas démarré ou l'endpoint n'existe pas
                <br/>
                → Vérifiez que <code>mvn spring-boot:run</code> est lancé
                <br/>
                → Testez avec : <code>curl http://localhost:8080/api/hr/employees</code>
              </li>
              <li>
                <strong>Si liste vide :</strong> Pas d'employés dans la base de données
                <br/>
                → Exécutez : <code>psql -U postgres -d zentra -f test_employees_data.sql</code>
                <br/>
                → Ou décommentez la section d'insertion dans le fichier SQL
              </li>
              <li>
                <strong>Si succès ici mais pas dans les autres pages :</strong> Problème dans les composants
                <br/>
                → Vérifiez la console du navigateur (F12)
                <br/>
                → Vérifiez que le <code>dataAPI</code> est bien importé
              </li>
            </ol>
          </div>
        </>
      )}
    </div>
  );
};

export default TestEmployeesPage;

