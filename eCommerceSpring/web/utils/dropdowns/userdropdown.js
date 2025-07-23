import { extractRole } from "../jwt/extractRole.js";

export function renderUserDropdown() {
    const jwt = sessionStorage.getItem("access_token");
    if (!jwt) {
        return
    }
    const role = extractRole(jwt);

    const dropdown = document.getElementById('user-dropdown')

    const firstItem = dropdown.children[0];
    const firstItemText = document.createElement('p');
    const firstItemIcon = document.createElement("i");
    firstItemIcon.classList.add("fa-solid", "fa-user");
    firstItemText.textContent = 'Profile';
    firstItem.firstChild.textContent = '';
    firstItem.firstChild.appendChild(firstItemIcon);
    firstItem.firstChild.appendChild(firstItemText);
    firstItem.firstChild.href = '../user/userinfo.html'


    const secondItem = dropdown.children[1];
    const secondItemText = document.createElement('p');
    const secondItemIcon = document.createElement("i");
    secondItemIcon.classList.add("fa-solid", "fa-cash-register");
    secondItemText.textContent = 'Purchases';
    secondItem.firstChild.textContent = ''
    secondItem.firstChild.appendChild(secondItemIcon);
    secondItem.firstChild.appendChild(secondItemText);
    secondItem.firstChild.href = '../purchase/userpurchases.html'


    if (role === 'ROLE_ADMIN') {
        const adminPanel = document.createElement('li');
        const adminPanelLink = document.createElement('a');
        adminPanelLink.classList.add('dropdown-item');
        adminPanelLink.href = `../user/adminpanel.html`;
        adminPanel.appendChild(adminPanelLink);
        const adminIcon = document.createElement("i")
        adminIcon.classList.add("fa-solid", "fa-user-tie");
        adminPanelLink.appendChild(adminIcon);
        const adminText = document.createElement('p')
        adminText.textContent = 'Admin Panel';
        adminPanelLink.appendChild(adminText);


        const adminStatistics = document.createElement('li');
        const adminStatisticsLink = document.createElement('a');
        adminStatisticsLink.classList.add('dropdown-item');
        adminStatisticsLink.href = '#';
        adminStatistics.appendChild(adminStatisticsLink);
        const statisticsIcon = document.createElement("i")
        statisticsIcon.classList.add("fa-solid", "fa-chart-pie");
        adminStatisticsLink.appendChild(statisticsIcon);
        const statisticsText = document.createElement('p')
        statisticsText.textContent = 'Statistics';
        adminStatisticsLink.appendChild(statisticsText);
        
        dropdown.appendChild(adminPanel);
        dropdown.appendChild(adminStatistics);
    }
}