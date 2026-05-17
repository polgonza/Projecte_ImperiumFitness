// Fragment suggerit per assistent IA - revisar i adaptar
/* =====================================================
   IMPERIUM FITNESS — i18n.js
   Motor d'internacionalització (català / anglès)
   -----------------------------------------------------
   ÚS:
     t("nav.inici")          → retorna el text en l'idioma actiu
     I18n.canviaIdioma("en") → canvia a anglès i re-renderitza
     I18n.idioma             → idioma actiu ("ca" | "en")

   A l'HTML, afegeix data-i18n="clau" a qualsevol element:
     <a href="index.html" data-i18n="nav.inici">Inici</a>
   Els placeholders dinàmics usen {0}, {1}...:
     t("toast.reservaOk", ["Spinning"])  → 'Reserva de "Spinning" confirmada!'
   ===================================================== */

const I18n = (() => {

  // ── Traduccions ──────────────────────────────────────
  const TRADUCCIONS = {

    ca: {
      // Navbar
      "nav.instalacions":       "Instal·lacions",
      "nav.classes":            "Classes",
      "nav.tarifes":            "Tarifes",
      "nav.botiga":             "Botiga",
      "nav.novetats":           "Novetats",
      "nav.contacte":           "Contacte",
      "nav.iniciSessio":        "Inicia sessió",
      "nav.uneix":              "Uneix-te",
      "nav.elMeuPerfil":        "El meu perfil",
      "nav.sortir":             "Sortir",
      "nav.tancarSessio":       "Tanca la sessió",
      "nav.registrat":          "Registra't",
      "nav.selector.ca":        "CA",
      "nav.selector.en":        "EN",

      // Index — Hero
      "hero.badge":             "La cadena de gimnasos premium",
      "hero.titol":             "Forja la teva millor versió",
      "hero.subtitol":          "Tecnologia d'ocupació en temps real, instal·lacions d'elit i una comunitat que t'impulsa. Entrena millor, entrena intel·ligent.",
      "hero.cta.uneix":         "Uneix-te ara →",
      "hero.cta.reserva":       "Reserva la teva classe",

      // Index — Ocupació
      "ocupacio.enviu":         "En directe",
      "ocupacio.titol":         "Vols saber quanta gent hi ha ara mateix?",
      "ocupacio.subtitol":      "Consulta l'ocupació de cada centre en temps real i evita les hores punta.",
      "ocupacio.cta":           "📊 Veure ocupació en temps real →",

      // Index — Serveis
      "serveis.titol":          "Serveis",
      "serveis.subtitol":       "Tot el que necessites sota un mateix sostre",
      "serveis.entrenament":    "Entrenament",
      "serveis.entrenamentDesc":"Equipament d'última generació i entrenadors certificats.",
      "serveis.nutrició":       "Nutrició",
      "serveis.nutricioDesc":   "Plans nutricionals personalitzats per professionals.",
      "serveis.recuperació":    "Recuperació",
      "serveis.recuperacioDesc":"Sauna, crioteràpia i zona wellness premium.",
      "serveis.classes":        "Classes Dirigides",
      "serveis.classesDesc":    "CrossFit, Ioga, Spinning, HIIT, Pilates i més.",

      // Index — Productes destacats
      "productes.titol":        "Productes Destacats",
      "productes.subtitol":     "Roba exclusiva i suplements d'alt rendiment",
      "productes.veureTotal":   "Veure tota la botiga →",

      // Index — Formulari ràpid
      "contacte.titol":         "Tens dubtes?",
      "contacte.subtitol":      "Escriu-nos i et responem en menys de 24h",
      "contacte.nom":           "Nom",
      "contacte.email":         "Correu electrònic",
      "contacte.missatge":      "El teu missatge...",
      "contacte.enviar":        "Enviar missatge",

      // Index — App
      "app.etiqueta":           "Descarrega l'App",
      "app.titol":              "El teu gimnàs a la butxaca",
      "app.subtitol":           "Consulta l'ocupació, reserva classes, gestiona el teu pla i compra a la botiga. Tot des d'una sola app.",
      "app.appStore":           "App Store",
      "app.googlePlay":         "Google Play",
      "app.disponibleA":        "Disponible a",
      "app.proximament":        "Aviat disponible",

      // Index — CTA final
      "cta.titol":              "Llest per al canvi?",
      "cta.subtitol":           "Uneix-te a la comunitat Imperium i transforma la teva vida. La primera setmana és gratuïta.",
      "cta.btn":                "Comença avui →",

      // Footer
      "footer.explorar":        "Explorar",
      "footer.mes":             "Més",
      "footer.newsletter":      "Newsletter",
      "footer.newsletterDesc":  "Ofertes exclusives i novetats.",
      "footer.privacitat":      "Política de Privacitat",
      "footer.cookies":         "Política de Cookies",
      "footer.termes":          "Termes i Condicions",
      "footer.normativa":       "Normativa de Clubs",
      "footer.canal":           "Canal Ètic",
      "footer.drets":           "© 2026 Imperium Fitness. Tots els drets reservats.",
      "footer.faq":             "Preguntes freqüents",
      "footer.ubicacio":        "Ubicació Gimnasos",
      "footer.convidar":        "Convidar un Amic",
      "footer.equip":           "Equip",
      "footer.descarrega":      "Descarrega l'App",
      "footer.desc":            "La cadena de gimnasos premium amb tecnologia d'ocupació en temps real. Transforma la teva vida.",

      // Carret
      "carret.titol":           "El meu carret",
      "carret.tancar":          "Tancar",
      "carret.total":           "Total",
      "carret.finalitzar":      "Finalitzar compra",
      "carret.buit":            "El carret és buit",

      // Login
      "login.titol":            "Benvingut",
      "login.subtitol":         "Accedeix al teu compte per reservar i comprar",
      "login.email":            "Correu electrònic",
      "login.contrasenya":      "Contrasenya",
      "login.entrar":           "Entrar",
      "login.oblidat":          "Has oblidat la contrasenya?",
      "login.noCompte":         "No tens compte?",
      "login.registrat":        "Registra't",
      "login.tornar":           "← Tornar a l'inici",
      "login.entrant":          "Entrant...",

      // Registre
      "registre.titol":         "Crea el teu compte",
      "registre.subtitol":      "Uneix-te a Imperium Fitness",
      "registre.nom":           "Nom complet",
      "registre.email":         "Correu electrònic",
      "registre.contrasenya":   "Contrasenya",
      "registre.confirmar":     "Confirma la contrasenya",
      "registre.crear":         "Crear compte",
      "registre.jaCompte":      "Ja tens compte?",
      "registre.inicia":        "Inicia sessió",
      "registre.creant":        "Creant compte...",
      "registre.placeholderNom":     "El teu nom",
      "registre.placeholderPass":    "Mínim 6 caràcters",
      "registre.placeholderConfirm": "Repeteix la contrasenya",
      "registre.termes":             "Accepto els",
      "registre.termesLink":         "Termes i Condicions",
      "registre.iLa":                "i la",
      "registre.privacitat":         "Política de Privacitat",

      // Tarifes
      "tarifes.titol":          "Tarifes",
      "tarifes.subtitol":       "Troba el pla perfecte per al teu estil d'entrenament",
      "tarifes.carregant":      "⏳ Carregant tarifes...",
      "tarifes.error":          "Error en carregar les tarifes. Torna-ho a intentar.",
      "tarifes.buit":           "No hi ha tarifes disponibles.",
      "tarifes.perDia":         "/dia",
      "tarifes.perMes":         "/mes",
      "tarifes.planActual":     "✓ Pla Actual",
      "tarifes.mesPopular":     "⭐ Més Popular",
      "tarifes.iniciarPerContractar": "Inicia sessió per contractar",
      "tarifes.comencarAra":    "Comença ara",
      "tarifes.seleccionar":    "Seleccionar",
      "tarifes.primeraSetmana": "✨ Primera setmana gratuïta · Sense permanència · Cancel·la quan vulguis",
      "tarifes.confirmar":      "Confirmes la contractació de \"{0}\" per {1}€?",
      "tarifes.activada":       "Tarifa \"{0}\" activada correctament! 🎉",
      "tarifes.errorActivar":   "No s'ha pogut activar la tarifa. Torna-ho a intentar.",
      "tarifes.cancelSub":      "Cancelar subscripció",
      "tarifes.canviarPla":     "Canviar Pla",

      // Activitats / Reserves
      "activitats.titol":       "Classes Dirigides",
      "activitats.subtitol":    "Selecciona un dia al calendari per veure les classes disponibles i reservar la teva plaça",
      "activitats.todesClasses":"Totes les classes",
      "activitats.filtreTotal": "Totes",
      "activitats.places":      "places",
      "activitats.plena":       "Completa",
      "activitats.reservar":    "Reservar",
      "activitats.reservada":   "Reservada",
      "activitats.carregant":   "⏳ Carregant classes...",
      "activitats.selecciona":  "Selecciona un dia",
      "activitats.perVeure":    "per veure les classes disponibles",
      "activitats.misReserves": "Les meves reserves",
      "activitats.confirmarRes":"Confirmar Reserva",
      "activitats.cancel":      "Cancel·lar",
      "activitats.confirmar":   "Confirmar",
      "activitats.carregantMsg":"Carregant...",

      // Perfil
      "perfil.titol":           "El meu Perfil",
      "perfil.subtitol":        "Gestiona el teu compte i consulta la teva activitat",
      "perfil.carregant":       "Carregant...",
      "perfil.email":           "Correu electrònic",
      "perfil.membre":          "Membre des de",
      "perfil.planActiu":       "Pla actiu",
      "perfil.senseTarifa":     "⚠️ Sense tarifa activa.\nContracta un pla per reservar classes.",
      "perfil.cancelSub":       "Cancelar subscripció",
      "perfil.canviarPla":      "Canviar Pla",
      "perfil.tancarSessio":    "Tanca la sessió",
      "perfil.tabReserves":     "Les meves Reserves",
      "perfil.tabPedidos":      "Els meus Pedidos",
      "perfil.tabHistorial":    "Historial de compres",
      "perfil.tabEstadistiques":"📊 Estadístiques",
      "perfil.tabGestioUsuaris":"👥 Gestió d'usuaris",
      "perfil.sensePla":        "Sense tarifa activa",
      "perfil.confirmCancel":   "Segur que vols cancelar la subscripció?\n\nSeguiràs tenint accés fins al final del període pagat.",
      "perfil.cancelOk":        "Subscripció cancelada. Accés fins al {0}.",
      "perfil.cancelError":     "No s'ha pogut cancelar. Torna-ho a intentar.",
      "perfil.tabGestioAdmin": "⚙️ Gestió Admin",


      "recover.titol":    "Recuperar Contrasenya",
      "recover.subtitol": "Introdueix el teu correu i t'enviarem instruccions per restablir la teva contrasenya",
      "recover.enviar":   "Enviar Instruccions",
      "recover.tornar":   "← Tornar al login",
      "recover.exitDesc": "Rebràs un email amb instruccions per restablir la teva contrasenya.",

      // Gestió usuaris (admin)
      "gestioUsuaris.titol":       "Gestió d'Usuaris",
      "gestioUsuaris.cerca":       "Cerca per nom o correu...",
      "gestioUsuaris.carregant":   "⏳ Carregant usuaris...",
      "gestioUsuaris.error":       "Error en carregar els usuaris.",
      "gestioUsuaris.buit":        "No s'han trobat usuaris.",
      "gestioUsuaris.nom":         "Nom",
      "gestioUsuaris.email":       "Correu",
      "gestioUsuaris.rol":         "Rol",
      "gestioUsuaris.data":        "Alta",
      "gestioUsuaris.accions":     "Accions",
      "gestioUsuaris.ferAdmin":    "Fer Admin",
      "gestioUsuaris.ferUser":     "Fer User",
      "gestioUsuaris.confirmar":   "Canviar el rol de {0} a {1}?",
      "gestioUsuaris.ok":          "Rol de {0} actualitzat a {1}.",
      "gestioUsuaris.error2":      "No s'ha pogut canviar el rol.",
      "gestioUsuaris.admin":       "Admin",
      "gestioUsuaris.user":        "Usuari",
      "gestioUsuaris.total":       "{0} usuaris",

      "blog.subtitol":              "Articles, esdeveniments i consells del món del fitness",
    "instalacions.subtitol":      "Espais d'elit dissenyats per portar el teu entrenament al màxim nivell",
    "instalacions.visitaDesc":    "Vols conèixer els nostres centres en persona?",
    "instalacions.visitaCta":     "Sol·licitar Visita Gratuïta",
    "contacte.paginaSubtitol":    "Tens alguna pregunta? Som aquí per ajudar-te",
    "contacte.nomPlaceholder":    "El teu nom",
    "contacte.missatgeLabel":     "Missatge",
    "contacte.missatgePlaceholder":"En què et podem ajudar?",
    "contacte.informacio":        "Informació",
    "contacte.horari":            "Dl–Dv: 06:00 – 23:00 · Cap de setmana: 08:00 – 21:00",
    "contacte.centresTitle":      "Els nostres Centres",  

      "ocupacio.titolPagina":    "OCUPACIÓ EN TEMPS REAL",
    "ocupacio.heroSub":        "Dades actualitzades cada 30 segons via sistema RFID de control d'accessos",
    "ocupacio.millorHora":     "MILLOR HORA AVUI",
    "ocupacio.menysOcupat":    "MENYS OCUPAT ARA",
    "ocupacio.prediccio":      "PREDICCIÓ 30 MIN",
    "ocupacio.prediccioSub":   "Reserva el teu lloc abans que s'ompli",
    "ocupacio.calculant":      "Calculant...",
    "ocupacio.cerca":          "Cerca gimnàs...",
    "ocupacio.baixa":          "Baixa (<40%)",
    "ocupacio.mitja":          "Mitja (40-70%)",
    "ocupacio.alta":           "Alta (>70%)",
    "ocupacio.comFunciona":    "Com funciona?",
    "ocupacio.comFuncionaDesc":"Cada centre Imperium compta amb un sistema de control d'accessos mitjançant polseres RFID. Quan un usuari entra o surt del gimnàs, els torns registren el moviment automàticament, actualitzant l'ocupació en temps real.",
  

      // Estadístiques
      "stats.resum":            "Resum General",
      "stats.totalUsuaris":     "Usuaris totals",
      "stats.nousM":            "Nous aquest mes",
      "stats.reservesActives":  "Reserves actives",
      "stats.classeMes":        "Classe més reservada",
      "stats.botiga":           "Botiga",
      "stats.topMes":           "Top producte del mes",
      "stats.topAny":           "Top producte de l'any",
      "stats.menysEstoc":       "Menys estoc",
      "stats.vendesMes":        "Vendes aquest mes",
      "stats.senseVendes":      "Sense vendes",
      "stats.error":            "No s'han pogut carregar les estadístiques.",

      "checkout.titol":           "Finalitzar Compra",
    "checkout.subtitol":        "Revisa la teva comanda i introdueix les dades de pagament",
    "checkout.loginRequerit":   "Has d'iniciar sessió",
    "checkout.loginDesc":       " per completar la compra. El teu carret es guardarà fins que tornis.",
    "checkout.dadesEnviament":  "Dades d'enviament",
    "checkout.dadesPagament":   "Dades de pagament",
    "checkout.campObligatori":  "Aquest camp és obligatori",
    "checkout.adreca":          "Adreça",
    "checkout.ciutat":          "Ciutat",
    "checkout.cp":              "Codi postal",
    "checkout.cpInvalid":       "Codi postal invàlid",
    "checkout.titular":         "Nom del titular",
    "checkout.numTarjeta":      "Número de targeta",
    "checkout.caducitat":       "Data de caducitat",
    "checkout.pagar":           "Pagar ara",
    "checkout.laTevaComanda":   "La teva comanda",
    "checkout.subtotal":        "Subtotal",
    "checkout.descompte":       "Descompte soci (5%)",
    "checkout.enviament":       "Enviament",
    "checkout.gratis":          "Gratuït",
    "checkout.tornarBotiga":    "← Tornar a la botiga",
    "checkout.pagamentFet":     "Pagament realitzat",
    "checkout.confirmacioDesc": "La teva comanda ha estat confirmada i està en preparació. Rebràs un correu amb el seguiment.",
    "checkout.seguirComprant":  "Continuar comprant",
    "checkout.versPedidos":     "Veure els meus pedidos",  

      "tienda.roba":      "Roba",
      "tienda.suplement": "Suplements",
      "tienda.accesoris": "Accessoris",  

      // Toast / missatges globals
      "toast.reservaOk":        "Reserva de \"{0}\" confirmada! 🎉",
      "toast.reservaError":     "No s'ha pogut fer la reserva.",
      "toast.cancelReservaOk":  "Reserva de \"{0}\" cancelada.",
      "toast.cancelReservaErr": "No s'ha pogut cancelar la reserva.",
      "toast.sessionExp":       "La sessió ha expirat. Inicia sessió de nou.",
      "toast.sessionAviat":     "⚠️ La sessió expirarà en menys de 5 minuts.",
      "toast.renovarSessio":    "Renovar sessió",
      "toast.ignorar":          "Ignorar",
      "toast.afegitCarret":     "\"{0}\" afegit al carret 🛒",
      "toast.missatgeEnviat":   "Missatge enviat! Et respondrem en menys de 24h 📩",
      "toast.subscrit":         "Subscrit correctament! 📬",
      "toast.emailInvalid":     "Introdueix un correu vàlid.",
      "toast.campsBuits":       "Omple tots els camps.",
      "toast.contraError":      "La contrasenya ha de tenir almenys 6 caràcters.",
      "toast.contraNoCoincid":  "Les contrasenyes no coincideixen.",
      "toast.benvingut":        "Benvingut! Redirigint...",
      "toast.compteCreat":      "Compte creat! Redirigint...",
      "toast.errorLogin":       "Credencials incorrectes.",
      "toast.sensePlanReserva": "Necessites un pla actiu per reservar classes.",
    },

    en: {
      // Navbar
      "nav.instalacions":       "Facilities",
      "nav.classes":            "Activities",
      "nav.tarifes":            "Pricing",
      "nav.botiga":             "Shop",
      "nav.novetats":           "News",
      "nav.contacte":           "Contact",
      "nav.iniciSessio":        "Log in",
      "nav.uneix":              "Join",
      "nav.elMeuPerfil":        "My profile",
      "nav.sortir":             "Log out",
      "nav.tancarSessio":       "Log out",
      "nav.registrat":          "Sign up",
      "nav.selector.ca":        "CA",
      "nav.selector.en":        "EN",

      // Index — Hero
      "hero.badge":             "The premium gym chain",
      "hero.titol":             "Forge your best self",
      "hero.subtitol":          "Real-time occupancy technology, elite facilities and a community that drives you forward. Train smarter, train better.",
      "hero.cta.uneix":         "Join now →",
      "hero.cta.reserva":       "Book a class",

      // Index — Ocupació
      "ocupacio.enviu":         "Live",
      "ocupacio.titol":         "Want to know how busy we are right now?",
      "ocupacio.subtitol":      "Check real-time occupancy at each centre and avoid peak hours.",
      "ocupacio.cta":           "📊 View live occupancy →",

      // Index — Serveis
      "serveis.titol":          "Services",
      "serveis.subtitol":       "Everything you need under one roof",
      "serveis.entrenament":    "Training",
      "serveis.entrenamentDesc":"State-of-the-art equipment and certified trainers.",
      "serveis.nutrició":       "Nutrition",
      "serveis.nutricioDesc":   "Personalised nutrition plans by professionals.",
      "serveis.recuperació":    "Recovery",
      "serveis.recuperacioDesc":"Sauna, cryotherapy and premium wellness zone.",
      "serveis.classes":        "Group Classes",
      "serveis.classesDesc":    "CrossFit, Yoga, Spinning, HIIT, Pilates and more.",

      // Index — Productes destacats
      "productes.titol":        "Featured Products",
      "productes.subtitol":     "Exclusive apparel and high-performance supplements",
      "productes.veureTotal":   "View all products →",

      // Index — Formulari ràpid
      "contacte.titol":         "Any questions?",
      "contacte.subtitol":      "Write to us and we'll reply within 24h",
      "contacte.nom":           "Name",
      "contacte.email":         "Email",
      "contacte.missatge":      "Your message...",
      "contacte.enviar":        "Send message",

      // Index — App
      "app.etiqueta":           "Download the App",
      "app.titol":              "Your gym in your pocket",
      "app.subtitol":           "Check occupancy, book classes, manage your plan and shop. All in one app.",
      "app.appStore":           "App Store",
      "app.googlePlay":         "Google Play",
      "app.disponibleA":        "Available on",
      "app.proximament":        "Coming soon",

      // Index — CTA final
      "cta.titol":              "Ready for a change?",
      "cta.subtitol":           "Join the Imperium community and transform your life. Your first week is free.",
      "cta.btn":                "Start today →",

      // Footer
      "footer.explorar":        "Explore",
      "footer.mes":             "More",
      "footer.newsletter":      "Newsletter",
      "footer.newsletterDesc":  "Exclusive offers and news.",
      "footer.privacitat":      "Privacy Policy",
      "footer.cookies":         "Cookie Policy",
      "footer.termes":          "Terms & Conditions",
      "footer.normativa":       "Club Rules",
      "footer.canal":           "Ethics Channel",
      "footer.drets":           "© 2026 Imperium Fitness. All rights reserved.",
      "footer.faq":             "FAQ",
      "footer.ubicacio":        "Gym Locations",
      "footer.convidar":        "Invite a Friend",
      "footer.equip":           "Team",
      "footer.descarrega":      "Download the App",
      "footer.desc":            "The premium gym chain with real-time occupancy technology. Transform your life.",

      // Carret
      "carret.titol":           "My cart",
      "carret.tancar":          "Close",
      "carret.total":           "Total",
      "carret.finalitzar":      "Checkout",
      "carret.buit":            "Your cart is empty",

      // Login
      "login.titol":            "Welcome back",
      "login.subtitol":         "Log in to book classes and shop",
      "login.email":            "Email",
      "login.contrasenya":      "Password",
      "login.entrar":           "Log in",
      "login.oblidat":          "Forgot your password?",
      "login.noCompte":         "Don't have an account?",
      "login.registrat":        "Sign up",
      "login.tornar":           "← Back to home",
      "login.entrant":          "Logging in...",

      "recover.titol":    "Reset Password",
      "recover.subtitol": "Enter your email and we'll send you instructions to reset your password",
      "recover.enviar":   "Send Instructions",
      "recover.tornar":   "← Back to login",
      "recover.exitDesc": "You'll receive an email with instructions to reset your password.",

      // Registre
      "registre.titol":         "Create your account",
      "registre.subtitol":      "Join Imperium Fitness",
      "registre.nom":           "Full name",
      "registre.email":         "Email",
      "registre.contrasenya":   "Password",
      "registre.confirmar":     "Confirm password",
      "registre.crear":         "Create account",
      "registre.jaCompte":      "Already have an account?",
      "registre.inicia":        "Log in",
      "registre.creant":        "Creating account...",
      "registre.placeholderNom":     "Your name",
      "registre.placeholderPass":    "At least 6 characters",
      "registre.placeholderConfirm": "Repeat your password",
      "registre.termes":             "I accept the",
      "registre.termesLink":         "Terms and Conditions",
      "registre.iLa":                "and the",
      "registre.privacitat":         "Privacy Policy",
      
      // Tarifes
      "tarifes.titol":          "Pricing",
      "tarifes.subtitol":       "Find the perfect plan for your training style",
      "tarifes.carregant":      "⏳ Loading plans...",
      "tarifes.error":          "Failed to load plans. Please try again.",
      "tarifes.buit":           "No plans available.",
      "tarifes.perDia":         "/day",
      "tarifes.perMes":         "/month",
      "tarifes.planActual":     "✓ Current Plan",
      "tarifes.mesPopular":     "⭐ Most Popular",
      "tarifes.iniciarPerContractar": "Log in to subscribe",
      "tarifes.comencarAra":    "Get started",
      "tarifes.seleccionar":    "Select",
      "tarifes.primeraSetmana": "✨ First week free · No commitment · Cancel anytime",
      "tarifes.confirmar":      "Confirm subscription to \"{0}\" for {1}€?",
      "tarifes.activada":       "Plan \"{0}\" activated successfully! 🎉",
      "tarifes.errorActivar":   "Could not activate plan. Please try again.",
      "tarifes.cancelSub":      "Cancel subscription",
      "tarifes.canviarPla":     "Change Plan",

      // Activitats / Reserves
      "activitats.titol":       "Group Classes",
      "activitats.subtitol":    "Select a day on the calendar to view available classes and book your spot",
      "activitats.todesClasses":"All classes",
      "activitats.filtreTotal": "All",
      "activitats.places":      "spots",
      "activitats.plena":       "Full",
      "activitats.reservar":    "Book",
      "activitats.reservada":   "Booked",
      "activitats.carregant":   "⏳ Loading classes...",
      "activitats.selecciona":  "Select a day",
      "activitats.perVeure":    "to see available classes",
      "activitats.misReserves": "My bookings",
      "activitats.confirmarRes":"Confirm Booking",
      "activitats.cancel":      "Cancel",
      "activitats.confirmar":   "Confirm",
      "activitats.carregantMsg":"Loading...",

      // Perfil
      "perfil.titol":           "My Profile",
      "perfil.subtitol":        "Manage your account and view your activity",
      "perfil.carregant":       "Loading...",
      "perfil.email":           "Email",
      "perfil.membre":          "Member since",
      "perfil.planActiu":       "Active plan",
      "perfil.senseTarifa":     "⚠️ No active plan.\nSubscribe to a plan to book classes.",
      "perfil.cancelSub":       "Cancel subscription",
      "perfil.canviarPla":      "Change Plan",
      "perfil.tancarSessio":    "Log out",
      "perfil.tabReserves":     "My Bookings",
      "perfil.tabPedidos":      "My Orders",
      "perfil.tabHistorial":    "Purchase history",
      "perfil.tabEstadistiques":"📊 Statistics",
      "perfil.tabGestioUsuaris":"👥 User management",
      "perfil.sensePla":        "No active plan",
      "perfil.confirmCancel":   "Are you sure you want to cancel your subscription?\n\nYou will retain access until the end of the paid period.",
      "perfil.cancelOk":        "Subscription cancelled. Access until {0}.",
      "perfil.cancelError":     "Could not cancel. Please try again.",
      "perfil.tabGestioAdmin": "⚙️ Admin Panel",

      // Gestió usuaris (admin)
      "gestioUsuaris.titol":       "User Management",
      "gestioUsuaris.cerca":       "Search by name or email...",
      "gestioUsuaris.carregant":   "⏳ Loading users...",
      "gestioUsuaris.error":       "Error loading users.",
      "gestioUsuaris.buit":        "No users found.",
      "gestioUsuaris.nom":         "Name",
      "gestioUsuaris.email":       "Email",
      "gestioUsuaris.rol":         "Role",
      "gestioUsuaris.data":        "Registered",
      "gestioUsuaris.accions":     "Actions",
      "gestioUsuaris.ferAdmin":    "Make Admin",
      "gestioUsuaris.ferUser":     "Make User",
      "gestioUsuaris.confirmar":   "Change role of {0} to {1}?",
      "gestioUsuaris.ok":          "Role of {0} updated to {1}.",
      "gestioUsuaris.error2":      "Could not change role.",
      "gestioUsuaris.admin":       "Admin",
      "gestioUsuaris.user":        "User",
      "gestioUsuaris.total":       "{0} users",

      "ocupacio.titolPagina":    "REAL-TIME OCCUPANCY",
    "ocupacio.heroSub":        "Data updated every 30 seconds via RFID access control system",
    "ocupacio.millorHora":     "BEST HOUR TODAY",
    "ocupacio.menysOcupat":    "LEAST BUSY NOW",
    "ocupacio.prediccio":      "30 MIN PREDICTION",
    "ocupacio.prediccioSub":   "Book your spot before it fills up",
    "ocupacio.calculant":      "Calculating...",
    "ocupacio.cerca":          "Search gym...",
    "ocupacio.baixa":          "Low (<40%)",
    "ocupacio.mitja":          "Medium (40-70%)",
    "ocupacio.alta":           "High (>70%)",
    "ocupacio.comFunciona":    "How does it work?",
    "ocupacio.comFuncionaDesc":"Each Imperium centre has an RFID access control system. When a user enters or leaves the gym, the turnstiles automatically register the movement, updating occupancy in real time.",  

      "blog.subtitol":              "Articles, events and fitness tips",
    "instalacions.subtitol":      "Elite spaces designed to take your training to the next level",
    "instalacions.visitaDesc":    "Want to visit our centres in person?",
    "instalacions.visitaCta":     "Request a Free Visit",
    "contacte.paginaSubtitol":    "Got a question? We're here to help",
    "contacte.nomPlaceholder":    "Your name",
    "contacte.missatgeLabel":     "Message",
    "contacte.missatgePlaceholder":"How can we help you?",
    "contacte.informacio":        "Information",
    "contacte.horari":            "Mon–Fri: 06:00 – 23:00 · Weekends: 08:00 – 21:00",
    "contacte.centresTitle":      "Our Centres",

      // Estadístiques
      "stats.resum":            "General Summary",
      "stats.totalUsuaris":     "Total users",
      "stats.nousM":            "New this month",
      "stats.reservesActives":  "Active bookings",
      "stats.classeMes":        "Most booked class",
      "stats.botiga":           "Shop",
      "stats.topMes":           "Top product this month",
      "stats.topAny":           "Top product this year",
      "stats.menysEstoc":       "Lowest stock",
      "stats.vendesMes":        "Sales this month",
      "stats.senseVendes":      "No sales",
      "stats.error":            "Could not load statistics.",

      "checkout.titol":           "Checkout",
    "checkout.subtitol":        "Review your order and enter your payment details",
    "checkout.loginRequerit":   "You need to log in",
    "checkout.loginDesc":       " to complete your purchase. Your cart will be saved.",
    "checkout.dadesEnviament":  "Shipping details",
    "checkout.dadesPagament":   "Payment details",
    "checkout.campObligatori":  "This field is required",
    "checkout.adreca":          "Address",
    "checkout.ciutat":          "City",
    "checkout.cp":              "Postcode",
    "checkout.cpInvalid":       "Invalid postcode",
    "checkout.titular":         "Cardholder name",
    "checkout.numTarjeta":      "Card number",
    "checkout.caducitat":       "Expiry date",
    "checkout.pagar":           "Pay now",
    "checkout.laTevaComanda":   "Your order",
    "checkout.subtotal":        "Subtotal",
    "checkout.descompte":       "Member discount (5%)",
    "checkout.enviament":       "Shipping",
    "checkout.gratis":          "Free",
    "checkout.tornarBotiga":    "← Back to shop",
    "checkout.pagamentFet":     "Payment confirmed",
    "checkout.confirmacioDesc": "Your order has been confirmed and is being prepared. You will receive a tracking email.",
    "checkout.seguirComprant":  "Continue shopping",
    "checkout.versPedidos":     "View my orders",  

      "tienda.roba":      "Clothing",
      "tienda.suplement": "Supplements",
      "tienda.accesoris": "Accessories",  

      // Toast / missatges globals
      "toast.reservaOk":        "Booking for \"{0}\" confirmed! 🎉",
      "toast.reservaError":     "Could not complete the booking.",
      "toast.cancelReservaOk":  "Booking for \"{0}\" cancelled.",
      "toast.cancelReservaErr": "Could not cancel the booking.",
      "toast.sessionExp":       "Your session has expired. Please log in again.",
      "toast.sessionAviat":     "⚠️ Your session will expire in less than 5 minutes.",
      "toast.renovarSessio":    "Renew session",
      "toast.ignorar":          "Dismiss",
      "toast.afegitCarret":     "\"{0}\" added to cart 🛒",
      "toast.missatgeEnviat":   "Message sent! We'll reply within 24h 📩",
      "toast.subscrit":         "Subscribed successfully! 📬",
      "toast.emailInvalid":     "Please enter a valid email.",
      "toast.campsBuits":       "Please fill in all fields.",
      "toast.contraError":      "Password must be at least 6 characters.",
      "toast.contraNoCoincid":  "Passwords do not match.",
      "toast.benvingut":        "Welcome! Redirecting...",
      "toast.compteCreat":      "Account created! Redirecting...",
      "toast.errorLogin":       "Incorrect credentials.",
      "toast.sensePlanReserva": "You need an active plan to book classes.",
    }
  };

  // ── Estat intern ─────────────────────────────────────
  const CLAU_STORAGE = "imperium_idioma";
  let _idioma = localStorage.getItem(CLAU_STORAGE) || "ca";

  // ── API pública ──────────────────────────────────────
  return {

    get idioma() { return _idioma; },

    /** Tradueix una clau. Els placeholders {0},{1}... es substitueixen per args[] */
    tradueix(clau, args = []) {
      const text = (TRADUCCIONS[_idioma] || TRADUCCIONS.ca)[clau];
      if (text === undefined) {
        console.warn(`[i18n] Clau no trobada: "${clau}"`);
        return clau;
      }
      return args.reduce((s, v, i) => s.replaceAll(`{${i}}`, v), text);
    },

    /** Canvia l'idioma, guarda la preferència i re-renderitza tots els elements data-i18n */
    canviaIdioma(nouIdioma) {
      if (!TRADUCCIONS[nouIdioma]) return;
      _idioma = nouIdioma;
      localStorage.setItem(CLAU_STORAGE, _idioma);
      this.aplicar();
      this._actualitzaSelector();
      // Dispara event perquè altres mòduls puguin reaccionar
      document.dispatchEvent(new CustomEvent("idioma:canvi", { detail: { idioma: _idioma } }));
    },

    /** Aplica les traduccions a tots els elements [data-i18n] de la pàgina */
    aplicar() {
      document.querySelectorAll("[data-i18n]").forEach(el => {
        const clau = el.getAttribute("data-i18n");
        const text = this.tradueix(clau);
        // Si és un input, actualitza el placeholder; si no, el textContent
        if (el.tagName === "INPUT" || el.tagName === "TEXTAREA") {
          el.placeholder = text;
        } else {
          el.textContent = text;
        }
      });
      // Actualitza el lang del <html>
      document.documentElement.lang = _idioma;
    },

    /** Actualitza l'estat visual del selector de la navbar */
    _actualitzaSelector() {
      document.querySelectorAll(".lang-btn").forEach(btn => {
        btn.classList.toggle("active", btn.dataset.lang === _idioma);
      });
    },

    /** Inicialitza el motor: aplica traduccions i munta el selector */
    init() {
      this.aplicar();
      this._actualitzaSelector();
    }
  };
})();

// Funció global d'accés ràpid
// Fragment suggerit per assistent IA - revisar i adaptar
function t(clau, args = []) {
  return I18n.tradueix(clau, args);
}