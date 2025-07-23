export function extractRole(jwt) {
    try {
        const base64Url = jwt.split(".")[1]; // Extrae el payload
        const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/"); // Convierte a Base64 estándar
        const jsonString = atob(base64); // Decodifica Base64 a texto plano
  
        const payload = JSON.parse(jsonString)
        return payload.ROLE || null
      } catch (error) {
        console.error("Error al extraer el atributo:", error);
        return null;
      }
}