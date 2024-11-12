// Sélectionne tous les liens de navigation dans la barre latérale
const sidebarLinks = document.querySelectorAll('.profil-sidebar a');

// Sélectionne toutes les sections de contenu du profil
const contentSections = document.querySelectorAll('.content-section');

/**
 * Masque toutes les sections de contenu
 */
function hideAllSections() {
    contentSections.forEach(section => {
        section.style.display = 'none';
    });
}

/**
 * Affiche une section spécifique en fonction de son ID
 * @param {string} sectionId - L'ID de la section à afficher
 */
function showSection(sectionId) {
    const targetSection = document.getElementById(sectionId);
    if (targetSection) {
        targetSection.style.display = 'block';
    }
}

/**
 * Met à jour l'état actif des liens de navigation
 * @param {Element} activeLink - Le lien de navigation actuellement sélectionné
 */
function updateActiveLink(activeLink) {
    sidebarLinks.forEach(link => link.classList.remove('active'));
    activeLink.classList.add('active');
}

// Ajoute un événement "click" à chaque lien de navigation
sidebarLinks.forEach(link => {
    link.addEventListener('click', function (event) {
        event.preventDefault(); // Empêche le comportement par défaut du lien

        // Récupère la section cible à partir de l'attribut `data-section`
        const targetSection = link.getAttribute('data-section');

        // Masque toutes les sections, affiche la section cible, et met à jour l'état actif
        hideAllSections();
        showSection(targetSection);
        updateActiveLink(link);
    });
});
