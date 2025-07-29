import { extractRole } from "../jwt/extractRole.js";

export function renderUserDropdown() {
	const jwt = sessionStorage.getItem("access_token");
	if (!jwt) {
		return;
	}
	const role = extractRole(jwt);

	const dropdown = document.getElementById("user-dropdown");

	const firstItem = dropdown.children[0];
	const firstItemText = document.createElement("p");
	const firstItemIcon = document.createElement("i");
	firstItemIcon.classList.add("fa-solid", "fa-user");
	firstItemText.textContent = "Profile";
	const firstItemLink = firstItem.querySelector("a");
	firstItemLink.innerHTML = "";
	firstItemLink.appendChild(firstItemIcon);
	firstItemLink.appendChild(firstItemText);
	firstItemLink.href = "../user/userinfo.html";

	const secondItem = dropdown.children[1];
	const secondItemText = document.createElement("p");
	const secondItemIcon = document.createElement("i");
	secondItemIcon.classList.add("fa-solid", "fa-cash-register");
	secondItemText.textContent = "Purchases";
	const secondItemLink = secondItem.querySelector("a");
	secondItemLink.innerHTML = "";
	secondItemLink.appendChild(secondItemIcon);
	secondItemLink.appendChild(secondItemText);
	secondItemLink.href = "../purchase/userpurchases.html";

    

	if (role === "ROLE_ADMIN") {
		const adminPanel = document.createElement("li");
		const adminPanelLink = document.createElement("a");
		adminPanelLink.classList.add("dropdown-item");
		adminPanelLink.href = `../user/adminpanel.html`;
		adminPanel.appendChild(adminPanelLink);
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

		dropdown.appendChild(adminPanel);
		dropdown.appendChild(adminStatistics);
	}

    const logout = document.createElement("li");
	const logoutLink = document.createElement("a");
	logoutLink.classList.add("dropdown-item");
	logout.appendChild(logoutLink);
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
    dropdown.appendChild(logout);
}
