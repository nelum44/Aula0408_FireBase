package com.example.aula04_08

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aula04_08.ui.theme.Aula0408Theme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {

val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Aula0408Theme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("login") {
                            LoginScreen(
                                onLogin = { userName ->
                                    navController.navigate("home/${userName}")
                                },
                                onRegisterClick = {
                                    navController.navigate("register")
                                }
                            )
                        }
                        // tela de registro; quando termina, volta pro login, e qnd clica em login, tbm vai pro login
                            composable("register") {
                                RegisterScreen(
                                    onRegisterComplete = {
                                        navController.navigate("login")
                                    },
                                    onLoginClick = {
                                        navController.navigate("login")
                                    }
                                )
                            }
                            composable(
                                "home/{userName}",
                                arguments = listOf(navArgument("userName") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val userName = backStackEntry.arguments?.getString("userName") ?: ""
                                HomeScreen(
                                    userName = userName,
                                    // qnd dá log out, retorna a tela de fazer login
                                    onLogout = {
                                        navController.navigate("login") {
                                            popUpTo("home/{userName}") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // construção da tela de registro
    @Composable
    fun RegisterScreen(
        onRegisterComplete: () -> Unit,
        onLoginClick: () -> Unit
    ) {
        var nome by remember { mutableStateOf("") }
        var apelido by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var senha by remember { mutableStateOf("") }
        var telefone by remember { mutableStateOf("") }
        var mostrarSenha by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }
        val db = Firebase.firestore

        // Cores do tema inicial registro
        val primaryColor = Color(0xFF5EA500) // cor verde tema inicial
        val primaryDarkColor = Color(0xFF4A8400)
        val textColor = Color.White
        val cardBackground = Color(0xFF1E1E1E) // fundo escuro tema inicial
        val textColorDark = Color.White // texto branco tema inicial
        val labelColor = Color.Gray // labels cinza tema inicial

        // fundo gradiente
        val gradient = Brush.verticalGradient(
            colors = listOf(Color(0xFF121212), Color(0xFF121212)) // cores qe estarão no gradiente
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradient),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.9f)
                    .verticalScroll(rememberScrollState()),
              //deixa o card arredondado
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(cardBackground)
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // logo ao top de tudo, com texto descritivo
                    Image(
                        painter = painterResource(id = R.drawable.logokitty),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(bottom = 16.dp)
                    )
                    //texto indicativo de tela
                    Text(
                        "Registro",
                        fontFamily = FontFamily.Cursive,
                        fontSize = 26.sp,
                        color = primaryColor,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    CustomDarkTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = "Nome",
                        backgroundColor = cardBackground,
                        textColor = textColorDark,
                        labelColor = labelColor
                    )

                    CustomDarkTextField(
                        value = apelido,
                        onValueChange = { apelido = it },
                        label = "Nickname",
                        backgroundColor = cardBackground,
                        textColor = textColorDark,
                        labelColor = labelColor
                    )

                    CustomDarkTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "E-mail",
                        backgroundColor = cardBackground,
                        textColor = textColorDark,
                        labelColor = labelColor
                    )

                    CustomDarkTextField(
                        value = senha,
                        onValueChange = { senha = it },
                        label = "Senha",
                        backgroundColor = cardBackground,
                        textColor = textColorDark,
                        labelColor = labelColor,
                        isPassword = true
                    )

                    CustomDarkTextField(
                        value = telefone,
                        onValueChange = { telefone = it },
                        label = "Telefone",
                        backgroundColor = cardBackground,
                        textColor = textColorDark,
                        labelColor = labelColor
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (nome.isBlank() || apelido.isBlank() || email.isBlank() || senha.isBlank()) {
                                errorMessage = "Preencha todos os campos obrigatórios"
                                return@Button
                            }

                            val usuario = hashMapOf(
                                "nome" to nome,
                                "apelido" to apelido,
                                "email" to email,
                                "senha" to senha,
                                "telefone" to telefone
                            )

                            db.collection("banco") // Usando a mesma coleção do primeiro código
                                .add(usuario)
                                .addOnSuccessListener {
                                    Log.d("Firestore", "Documento adicionado com ID: ${it.id}")
                                    onRegisterComplete()
                                }
                                .addOnFailureListener { e ->
                                    errorMessage = "Erro ao cadastrar: ${e.message}"
                                    Log.w("Firestore", "Erro ao adicionar documento", e)
                                }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor,
                            contentColor = textColor
                        ),
                        shape = RoundedCornerShape(10.dp) // Bordas arredondadas como no primeiro código
                    ) {
                        Text("Cadastrar", fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            onLoginClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = primaryColor
                        ),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, primaryColor)
                    ) {
                        Text("Já tem uma conta? Faça login", fontSize = 16.sp)
                    }
                }
            }
        }
    }


    //tela de cadastro e home, onde cadastra informações
    @Composable
    fun HomeScreen(
        userName: String = "Usuário",
        onLogout: () -> Unit
    ) {
        var menuExpanded by remember { mutableStateOf(false) }
        var mostrarRegistros by remember { mutableStateOf(false) }
        val db = Firebase.firestore
        val banco = remember { mutableStateListOf<Map<String, Any>>() }
        val scrollState = rememberScrollState() // add scroll

        // cores do tema inicial
        val backgroundColor = Color(0xFF121212)
        val primaryColor = Color(0xFF5EA500)
        val textColor = Color.White
        val cardBackground = Color(0xFF1E1E1E)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            // define o menu no topo, fora do scroll
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = primaryColor
                    )
                }
                // add dropdown de menu e seus itens
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Listar Registros") },
                        onClick = {
                            menuExpanded = false
                            db.collection("banco")
                                .get()
                                .addOnSuccessListener { result ->
                                    banco.clear()
                                    for (document in result) {
                                        banco.add(document.data)
                                    }
                                    mostrarRegistros = true
                                }
                                .addOnFailureListener { exception ->
                                    Log.w(TAG, "Error getting documents.", exception)
                                }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Sair") },
                        onClick = {
                            menuExpanded = false
                            onLogout()
                        }
                    )
                }

            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image( // logo do perfil
                    painter = painterResource(id = R.drawable.logokitty),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp)
                )
            }

            // Conteúdo rolável
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState) // Adicionando scroll aqui
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text( // dps do login
                    "Bem-vindo, $userName!",
                    fontFamily = FontFamily.Cursive,
                    fontSize = 26.sp,
                    color = primaryColor,
                    modifier = Modifier.padding(vertical = 24.dp)
                )

                    // botão de exibição de registros do banco; quando aperta mostra todas as variáveis do banco
                if (mostrarRegistros) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 40.dp) // Adicionando padding bottom para espaço
                    ) {
                        banco.forEachIndexed { index, registro ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .background(cardBackground, shape = RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Text("Registro ${index + 1}", color = primaryColor, fontSize = 18.sp)
                                Text("Nome: ${registro["nome"]}", color = textColor)
                                Text("Apelido: ${registro["apelido"]}", color = textColor)
                                Text("Email: ${registro["email"]}", color = textColor)
                                Text("Senha: ${registro["senha"]}", color = textColor)
                                Text("Telefone: ${registro["telefone"]}", color = textColor)
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Use o menu no canto superior direito para listar os registros",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 32.dp)
                            .padding(horizontal = 24.dp)
                    )
                }
            }
        }
    }


    //login e cadastro
    @Composable
    fun LoginScreen(
        onLogin: (String) -> Unit,
        onRegisterClick: () -> Unit
    ) {
        //variáveis
        var email by remember { mutableStateOf("") }
        var senha by remember { mutableStateOf("") }
        var mostrarSenha by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }
        val db = Firebase.firestore

        // Cores do tema do primeiro código
        val backgroundColor = Color(0xFF121212)
        val primaryColor = Color(0xFF5EA500)
        val textColor = Color.White
        val cardBackground = Color(0xFF1E1E1E)
        val labelColor = Color.Gray
            // coluna principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // fixando logo no topo
            Image(
                painter = painterResource(id = R.drawable.logokitty),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp)
            )
            // título login
            Text(
                "Login",
                fontFamily = FontFamily.SansSerif,
                fontSize = 26.sp,
                color = primaryColor,
                modifier = Modifier.padding(vertical = 24.dp)
            )
            // mostra mensagem de erro em vermelho caso tenha uma
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // campo email
            CustomDarkTextField(
                value = email,
                onValueChange = { email = it },
                label = "E-mail",
                backgroundColor = cardBackground,
                textColor = textColor,
                labelColor = labelColor
            )
            // campo senha
            CustomDarkTextField(
                value = senha,
                onValueChange = { senha = it },
                label = "Senha",
                backgroundColor = cardBackground,
                textColor = textColor,
                labelColor = labelColor,
                isPassword = !mostrarSenha,
                trailingIcon = {
                    IconButton(onClick = { mostrarSenha = !mostrarSenha }) {
                        Icon(
                            painter = painterResource(
                                // Se mostrarSenha estiver true deixa a senha invisível, caso contrário deixa visível usando imagens
                                id = if (mostrarSenha) R.drawable.visivel else R.drawable.invisivel
                            ),
                            contentDescription = "Toggle password visibility",
                            tint = labelColor
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isBlank() || senha.isBlank()) {
                        // retorna erro caso o usuário não preencha algum dos campos
                        errorMessage = "Preencha todos os campos"
                        return@Button
                    }

                    db.collection("banco")
                        .whereEqualTo("email", email)
                        .whereEqualTo("senha", senha)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                // retorna erro caso o usuário digite algo inválido
                                errorMessage = "Credenciais inválidas"
                            } else {
                                val nomeUsuario = documents.documents[0].getString("apelido") ?: email
                                onLogin(nomeUsuario)
                            }
                        }
                        // pega erro e mostra caso o login seja mal sucedido
                        .addOnFailureListener { exception ->
                            errorMessage = "Erro ao fazer login: ${exception.message}"
                            Log.w("Login", "Erro ao verificar login", exception)
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Entrar", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // botão q leva pro registro
            Button(
                onClick = {
                    onRegisterClick()
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = primaryColor
                ),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, primaryColor)
            ) {
                Text("Não tem conta? Cadastre-se", fontSize = 16.sp)
            }
        }
    }

    //troca o tema inicial por tema escuro
    @Composable

    fun CustomDarkTextField(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        backgroundColor: Color,
        textColor: Color,
        labelColor: Color,
        isPassword: Boolean = false,
        trailingIcon: @Composable (() -> Unit)? = null
    ) {    val primaryDarkColor = Color(0xFF4A8400)

        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, color = labelColor) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                cursorColor = primaryDarkColor,
                focusedLabelColor = labelColor,
                unfocusedLabelColor = labelColor,
                focusedIndicatorColor = primaryDarkColor,
                unfocusedIndicatorColor = Color.Gray
            ),
            trailingIcon = trailingIcon
        )
    }


    @Preview
    @Composable
    fun LoginPreview() {
        Aula0408Theme {
            LoginScreen(onLogin = {}, onRegisterClick = {})
        }
    }

    @Preview
    @Composable
    fun RegisterPreview() {
        Aula0408Theme {
            RegisterScreen(onRegisterComplete = {}, onLoginClick = {})
        }
    }

    @Preview
    @Composable
    fun HomePreview() {
        Aula0408Theme {
        }
    }