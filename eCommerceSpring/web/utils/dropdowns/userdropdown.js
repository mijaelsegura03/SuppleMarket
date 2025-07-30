import { extractRole } from "../jwt/extractRole.js";

export function renderUserDropdown() {
	const jwt = sessionStorage.getItem("access_token");
	const dropdown = document.getElementById("user-dropdown");
	if (!jwt) {
		const loginItem = document.createElement('li')
		const loginItemText = document.createElement('p')
		loginItemText.textContent = 'Login'
		const loginItemLink = document.createElement('a')
		loginItemLink.href = "../auth/login/login.html"
		loginItemLink.appendChild(loginItemText)
		loginItemLink.classList.add('dropdown-item')
		loginItem.appendChild(loginItemLink)

		const registerItem = document.createElement('li')
		const registerItemText = document.createElement('p')
		registerItemText.textContent = 'Register'
		const registerItemLink = document.createElement('a')
		registerItemLink.href = "../auth/register/register.html"
		registerItemLink.appendChild(registerItemText)
		registerItemLink.classList.add('dropdown-item')
		registerItem.appendChild(registerItemLink)
		
		dropdown.appendChild(loginItem)
		dropdown.appendChild(registerItem)
		return;
	}
	const role = extractRole(jwt);

	const profileItem = document.createElement('li')
	const profileItemText = document.createElement("p");
	const profileItemIcon = document.createElement("i");
	profileItemIcon.classList.add("fa-solid", "fa-user");
	profileItemText.textContent = "Profile";
	const profileItemLink = document.createElement("a");
	profileItemLink.innerHTML = "";
	profileItemLink.appendChild(profileItemIcon);
	profileItemLink.appendChild(profileItemText);
	profileItemLink.href = "../user/userinfo.html";
	profileItemLink.classList.add('dropdown-item')
	profileItem.appendChild(profileItemLink)

	const purchasesItem = document.createElement('li');
	const purchasesItemText = document.createElement("p");
	const purchasesItemIcon = document.createElement("i");
	purchasesItemIcon.classList.add("fa-solid", "fa-cash-register");
	purchasesItemText.textContent = "Purchases";
	const purchasesItemLink = document.createElement("a");
	purchasesItemLink.innerHTML = "";
	purchasesItemLink.appendChild(purchasesItemIcon);
	purchasesItemLink.appendChild(purchasesItemText);
	purchasesItemLink.href = "../purchase/userpurchases.html";
	purchasesItemLink.classList.add('dropdown-item')
	purchasesItem.appendChild(purchasesItemLink)

	dropdown.appendChild(profileItem)
	dropdown.appendChild(purchasesItem)

	if (role === "ROLE_ADMIN") {
		const adminPanelItem = document.createElement("li");
		const adminPanelLink = document.createElement("a");
		adminPanelLink.classList.add("dropdown-item");
		adminPanelLink.href = `../user/adminpanel.html`;
		adminPanelItem.appendChild(adminPanelLink);
		const adminIcon = document.createElement("i");
		adminIcon.classList.add("fa-solid", "fa-user-tie");
		adminPanelLink.appendChild(adminIcon);
		const adminText = document.createElement("p");
		adminText.textContent = "Admin Panel";
		adminPanelLink.appendChild(adminText);

		const adminStatistics = document.createElement("li");
		const adminStatisticsLink = document.createElement("a");
		adminStatisticsLink.classList.add("dropdown-item");
		adminStatisticsLink.href = "../statistics/statistics.html";
		adminStatistics.appendChild(adminStatisticsLink);
		const statisticsIcon = document.createElement("i");
		statisticsIcon.classList.add("fa-solid", "fa-chart-pie");
		adminStatisticsLink.appendChild(statisticsIcon);
		const statisticsText = document.createElement("p");
		statisticsText.textContent = "Statistics";
		adminStatisticsLink.appendChild(statisticsText);

		dropdown.appendChild(adminPanelItem);
		dropdown.appendChild(adminStatistics);
	}

    const logoutItem = document.createElement("li");
	const logoutLink = document.createElement("a");
	logoutLink.classList.add("dropdown-item");
	logoutItem.appendChild(logoutLink);
	const logoutIcon = document.createElement("i");
	logoutIcon.classList.add("fa-solid", "fa-circle-xmark");
	logoutLink.appendChild(logoutIcon);
	const logoutText = document.createElement("p");
	logoutText.textContent = "Logout";
	logoutLink.appendChild(logoutText);
    logoutLink.addEventListener("click", () => {
        sessionStorage.removeItem("access_token");
    });
    logoutLink.href = "../home/home.html";
    dropdown.appendChild(logoutItem);
}
