package com.example.proyectosemestral20

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF4F46E5),
                    secondary = Color(0xFF0EA5E9),
                    tertiary = Color(0xFF10B981),
                    background = Color(0xFFF8FAFC),
                    surface = Color.White
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavegacion()
                }
            }
        }
    }
}

enum class PerfilUsuario {
    ESTUDIANTE,
    ASISTENTE,
    DOCENTE
}

enum class Pantalla {
    LOGIN,
    MENU_ESTUDIANTE,
    MODULO_LECTURA,
    MODULO_MATEMATICA,
    PANEL_ASISTENTE,
    PANEL_DOCENTE
}

// Estado global ficticio para simular la asignación con Código Temporal
object EstadoSesionFicticia {
    var codigoGenerado by mutableStateOf("1234")
    var estudianteAsignado by mutableStateOf("Estudiante Ficticio 1")
    var cursoAsignado by mutableStateOf("2.º Básico")
    var dificultadAsignada by mutableStateOf("Básica")
    var intentosRealizados by mutableStateOf(0)
    var ultimoResultado by mutableStateOf("Sin realizar")
    var observacionAsistente by mutableStateOf("")
}

@Composable
fun AppNavegacion() {
    var pantallaActual by remember { mutableStateOf(Pantalla.LOGIN) }
    var perfilActual by remember { mutableStateOf(PerfilUsuario.ESTUDIANTE) }

    Crossfade(targetState = pantallaActual, label = "TransicionNavegacion") { pantalla ->
        when (pantalla) {
            Pantalla.LOGIN -> PantallaLogin(
                onIngresoExitoso = { perfil ->
                    perfilActual = perfil
                    pantallaActual = when (perfil) {
                        PerfilUsuario.ESTUDIANTE -> Pantalla.MENU_ESTUDIANTE
                        PerfilUsuario.ASISTENTE -> Pantalla.PANEL_ASISTENTE
                        PerfilUsuario.DOCENTE -> Pantalla.PANEL_DOCENTE
                    }
                }
            )
            Pantalla.MENU_ESTUDIANTE -> PantallaMenuEstudiante(
                onIrALectura = { pantallaActual = Pantalla.MODULO_LECTURA },
                onIrAMatematica = { pantallaActual = Pantalla.MODULO_MATEMATICA },
                onCerrarSesion = { pantallaActual = Pantalla.LOGIN }
            )
            Pantalla.MODULO_LECTURA -> PantallaLectura(
                onVolver = { pantallaActual = Pantalla.MENU_ESTUDIANTE }
            )
            Pantalla.MODULO_MATEMATICA -> PantallaMatematica(
                onVolver = { pantallaActual = Pantalla.MENU_ESTUDIANTE }
            )
            Pantalla.PANEL_ASISTENTE -> PantallaPanelAsistente(
                onCerrarSesion = { pantallaActual = Pantalla.LOGIN }
            )
            Pantalla.PANEL_DOCENTE -> PantallaPanelDocente(
                onCerrarSesion = { pantallaActual = Pantalla.LOGIN }
            )
        }
    }
}

@Composable
fun PantallaLogin(onIngresoExitoso: (PerfilUsuario) -> Unit) {
    var perfilSeleccionado by remember { mutableStateOf(PerfilUsuario.ESTUDIANTE) }
    var codigoIngresado by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFFEEF2FF), Color(0xFFF8FAFC)))),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(56.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "MPAZ NEB",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Refuerzo Lectura y Matemática (1.º a 4.º Básico)",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Selecciona perfil de acceso:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = perfilSeleccionado == PerfilUsuario.ESTUDIANTE,
                        onClick = { perfilSeleccionado = PerfilUsuario.ESTUDIANTE },
                        label = { Text("Estudiante (con Código)") },
                        leadingIcon = { if (perfilSeleccionado == PerfilUsuario.ESTUDIANTE) Icon(Icons.Default.Check, null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    FilterChip(
                        selected = perfilSeleccionado == PerfilUsuario.ASISTENTE,
                        onClick = { perfilSeleccionado = PerfilUsuario.ASISTENTE },
                        label = { Text("Asistente de Aula") },
                        leadingIcon = { if (perfilSeleccionado == PerfilUsuario.ASISTENTE) Icon(Icons.Default.Check, null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    FilterChip(
                        selected = perfilSeleccionado == PerfilUsuario.DOCENTE,
                        onClick = { perfilSeleccionado = PerfilUsuario.DOCENTE },
                        label = { Text("Administración / Docente") },
                        leadingIcon = { if (perfilSeleccionado == PerfilUsuario.DOCENTE) Icon(Icons.Default.Check, null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (perfilSeleccionado == PerfilUsuario.ESTUDIANTE) {
                    OutlinedTextField(
                        value = codigoIngresado,
                        onValueChange = { codigoIngresado = it },
                        label = { Text("Código temporal de sesión") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Código de prueba activo: ${EstadoSesionFicticia.codigoGenerado}",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (perfilSeleccionado == PerfilUsuario.ESTUDIANTE) {
                            if (codigoIngresado.trim() == EstadoSesionFicticia.codigoGenerado) {
                                onIngresoExitoso(PerfilUsuario.ESTUDIANTE)
                            } else {
                                Toast.makeText(context, "Código inválido Ingresa: ${EstadoSesionFicticia.codigoGenerado}", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            onIngresoExitoso(perfilSeleccionado)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Ingresar a la Sesión", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun PantallaMenuEstudiante(
    onIrALectura: () -> Unit,
    onIrAMatematica: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "¡Hola, ${EstadoSesionFicticia.estudianteAsignado}!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Nivel: ${EstadoSesionFicticia.cursoAsignado} • Dificultad: ${EstadoSesionFicticia.dificultadAsignada}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        ElevatedCard(
            onClick = onIrALectura,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Módulo de Lectura", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Comprensión lectora y vocabulario", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCard(
            onClick = onIrAMatematica,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Build, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Módulo de Matemática", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Ejercicios numéricos y razonamiento", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = onCerrarSesion,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Icon(Icons.Default.Close, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Salir de la Sesión")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLectura(onVolver: () -> Unit) {
    var opcionSeleccionada by remember { mutableStateOf("") }
    var mensajeFeedback by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Módulo de Lectura", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Lectura Corta: El zorro y la chinche", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Un pequeño zorro caminaba por el bosque de San Bernardo cuando vio una chinche brillante sobre una hoja. " +
                                "Le preguntó su nombre y la chinche le respondió amablemente cantando",
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Pregunta de Comprensión:", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text("¿Dónde se encontraba la chinche brillante?", fontSize = 13.sp, color = Color.DarkGray)

            Spacer(modifier = Modifier.height(10.dp))

            val opciones = listOf("En un río", "Sobre una hoja", "En una cueva")
            opciones.forEach { opcion ->
                OutlinedButton(
                    onClick = { opcionSeleccionada = opcion },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (opcionSeleccionada == opcion) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                    )
                ) {
                    Text(opcion)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    EstadoSesionFicticia.intentosRealizados++
                    if (opcionSeleccionada == "Sobre una hoja") {
                        mensajeFeedback = "¡Excelente! Respuesta correcta"
                        EstadoSesionFicticia.ultimoResultado = "Correcto (Lectura)"
                    } else {
                        mensajeFeedback = "Inténtalo nuevamente Vuelve a leer el texto con atención"
                        EstadoSesionFicticia.ultimoResultado = "Reintento (Lectura)"
                    }
                },
                enabled = opcionSeleccionada.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Enviar Respuesta")
            }

            if (mensajeFeedback.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (EstadoSesionFicticia.ultimoResultado.contains("Correcto")) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = mensajeFeedback,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = if (EstadoSesionFicticia.ultimoResultado.contains("Correcto")) Color(0xFF166534) else Color(0xFF991B1B)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMatematica(onVolver: () -> Unit) {
    var respuestaInput by remember { mutableStateOf("") }
    var mensajeFeedback by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Módulo de Matemática", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Ejercicio (1.º a 4.º Básico)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Si tienes 12 manzanas en una cesta y regalas 4 a tus compañeros, ¿cuántas manzanas te quedan?", fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = respuestaInput,
                onValueChange = { respuestaInput = it },
                label = { Text("Escribe tu resultado numérico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    EstadoSesionFicticia.intentosRealizados++
                    if (respuestaInput.trim() == "8") {
                        mensajeFeedback = "¡Muy bien! 12 - 4 = 8"
                        EstadoSesionFicticia.ultimoResultado = "Correcto (Matemática)"
                    } else {
                        mensajeFeedback = "Revisa tu resta e intenta otra vez"
                        EstadoSesionFicticia.ultimoResultado = "Reintento (Matemática)"
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Comprobar Resultado")
            }

            if (mensajeFeedback.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (EstadoSesionFicticia.ultimoResultado.contains("Correcto")) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = mensajeFeedback,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = if (EstadoSesionFicticia.ultimoResultado.contains("Correcto")) Color(0xFF166534) else Color(0xFF991B1B)
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaPanelAsistente(onCerrarSesion: () -> Unit) {
    var observacionLocal by remember { mutableStateOf(EstadoSesionFicticia.observacionAsistente) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Panel Asistente de Aula", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Asignación de actividades y seguimiento pedagógico", fontSize = 12.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Generar Sesión Ficticia", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Código Temporal: ${EstadoSesionFicticia.codigoGenerado}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text("Estudiante: ${EstadoSesionFicticia.estudianteAsignado}")
                Text("Curso: ${EstadoSesionFicticia.cursoAsignado} | Dificultad: ${EstadoSesionFicticia.dificultadAsignada}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Registro de Avance Ficticio", fontWeight = FontWeight.Bold)
                Text("Intentos totales: ${EstadoSesionFicticia.intentosRealizados}")
                Text("Estado actual: ${EstadoSesionFicticia.ultimoResultado}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = observacionLocal,
            onValueChange = { observacionLocal = it },
            label = { Text("Registrar Observación Pedagógica Ficticia") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                EstadoSesionFicticia.observacionAsistente = observacionLocal
                Toast.makeText(context, "Observación guardada correctamente", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Observación")
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Close, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar Sesión")
        }
    }
}

@Composable
fun PantallaPanelDocente(onCerrarSesion: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Panel Administración / Docente", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Gestión del catálogo de actividades (1.º a 4.º Básico)", fontSize = 12.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Catálogo de Actividades Autorizadas", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("• Lectura: Comprensión básica (Disponible)")
                Text("• Matemática: Sumas y Restas simples (Disponible)")
                Text("• Lectura: Vocabulario contextual (Borrador)")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Acción para agregar nuevas actividades */ }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Add, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Crear Nueva Actividad de Prueba")
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Close, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar Sesión")
        }
    }
}