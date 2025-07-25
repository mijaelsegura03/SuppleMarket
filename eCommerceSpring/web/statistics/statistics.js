import { extractDni } from "../utils/jwt/extractDni.js";
import { renderUserDropdown } from "../utils/dropdowns/userdropdown.js";
const USER_PURCHASES_URL = "http://localhost:8080/purchases/dni";
const PURCHASES_URL = "http://localhost:8080/purchases";
const SUPPLEMENTS_URL = "http://localhost:8080/supplements";

document.addEventListener("DOMContentLoaded", () => {
	renderUserDropdown();
	renderStatistics();
});

async function renderStatistics() {
	const purchases = await fetchPurchases();
	const fiveMostSoldSupplements = getMostSoldSupplements(purchases);
	renderMostSoldSupplements(fiveMostSoldSupplements);
	renderMonthlySalesAmount(purchases);
	renderMonthlySales(purchases);
}

async function fetchPurchases() {
	const jwt = sessionStorage.getItem("access_token");
	const response = await fetch(`${PURCHASES_URL}`, {
		headers: {
			Authorization: `Bearer ${jwt}`,
		},
	});
	const data = await response.json();
	return data.purchases;
}

async function renderMostSoldSupplements(fiveMostSoldSupplements) {
	const jwt = sessionStorage.getItem("access_token");
	let labels = [];
	let data = [];
	for (const supplement of fiveMostSoldSupplements) {
		const supplementResponse = await fetch(
			`${SUPPLEMENTS_URL}/${supplement[0]}`,
			{
				headers: {
					Authorization: `Bearer ${jwt}`,
				},
			}
		);
		const supplementData = await supplementResponse.json();
		labels.push(supplementData.supplement.name);
		data.push(supplement[1]);
	}
	const chartData = {
		labels: labels,
		datasets: [
			{
				label: "Quantity Sold",
				data: data,
				backgroundColor: [
					"rgba(227, 12, 58, 1)",
					"rgb(255, 205, 86)",
					"rgb(54, 162, 235)",
					"rgba(21, 133, 1, 1)",
					"rgba(19, 8, 185, 1)",
				],
				hoverOffset: 4,
			},
		],
	};
	const config = {
		type: "pie",
		data: chartData,
		options: {
			plugins: {
				title: {
					display: true,
					text: "Most Sold Products",
					position: "top",
					font: {
						size: 40,
						weight: "bold",
						family: "Montserrat, sans-serif",
					},
					color: "rgb(0, 0, 0)",
				},
			},
		},
	};
	const chart = document.getElementById("most-sold-supplements");

	new Chart(chart, config);
}

function getMostSoldSupplements(purchases) {
	const supplementsMap = {};
	purchases.forEach((purchase) => {
		const purchaseDetails = purchase.purchaseDetails;
		purchaseDetails.forEach((detail) => {
			if (!supplementsMap[detail.supplementId]) {
				supplementsMap[detail.supplementId] = 0;
			}
			supplementsMap[detail.supplementId] += detail.quantity;
		});
	});
	const sortedSupplements = Object.entries(supplementsMap).sort(
		([, valueA], [, valueB]) => valueB - valueA
	);
	return sortedSupplements.slice(0, 5);
}

function renderMonthlySalesAmount(purchases) {
	const chart = document.getElementById("monthly-sales-amount");
	const labels = [
		"January",
		"February",
		"March",
		"April",
		"May",
		"June",
		"July",
		"August",
		"September",
		"October",
		"November",
		"December",
	];
	const data = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
	purchases.forEach((purchase) => {
		const date = new Date(purchase.purchaseDate);
		const month = date.getMonth();
		const amount = purchase.totalPrice;
		data[month] += amount;
	});
	const chartData = {
		labels: labels,
		datasets: [
			{
				label: "Monthly Sales Amount",
				data: data,
				backgroundColor: [
					"rgba(255, 99, 132, 0.2)",
					"rgba(255, 159, 64, 0.2)",
					"rgba(255, 205, 86, 0.2)",
					"rgba(75, 192, 192, 0.2)",
					"rgba(54, 162, 235, 0.2)",
					"rgba(153, 102, 255, 0.2)",
					"rgba(201, 203, 207, 0.2)",
				],
				borderColor: [
					"rgb(255, 99, 132)",
					"rgb(255, 159, 64)",
					"rgb(255, 205, 86)",
					"rgb(75, 192, 192)",
					"rgb(54, 162, 235)",
					"rgb(153, 102, 255)",
					"rgb(201, 203, 207)",
				],
				borderWidth: 1,
			},
		],
	};
	const config = {
		type: "bar",
		data: chartData,
		options: {
			scales: {
				y: {
					beginAtZero: true,
				},
			},
			plugins: {
				title: {
					display: true,
					text: "Monthly Sales Amount",
					position: "top",
					font: {
						size: 40,
						weight: "bold",
						family: "Montserrat, sans-serif",
					},
					color: "rgb(0, 0, 0)",
				},
			},
		},
	};
	new Chart(chart, config);
}

function renderMonthlySales(purchases) {
	const chart = document.getElementById("monthly-sales");
	const labels = [
		"January",
		"February",
		"March",
		"April",
		"May",
		"June",
		"July",
		"August",
		"September",
		"October",
		"November",
		"December",
	];
	const data = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
	purchases.forEach((purchase) => {
		const date = new Date(purchase.purchaseDate);
		const month = date.getMonth();
		data[month]++;
	});
	const chartData = {
		labels: labels,
		datasets: [
			{
				label: "Monthly Sales",
				data: data,
				backgroundColor: [
					"rgba(255, 99, 132, 0.2)",
					"rgba(255, 159, 64, 0.2)",
					"rgba(255, 205, 86, 0.2)",
					"rgba(75, 192, 192, 0.2)",
					"rgba(54, 162, 235, 0.2)",
					"rgba(153, 102, 255, 0.2)",
					"rgba(201, 203, 207, 0.2)",
				],
				borderColor: [
					"rgb(255, 99, 132)",
					"rgb(255, 159, 64)",
					"rgb(255, 205, 86)",
					"rgb(75, 192, 192)",
					"rgb(54, 162, 235)",
					"rgb(153, 102, 255)",
					"rgb(201, 203, 207)",
				],
				borderWidth: 1,
			},
		],
	};
	const config = {
		type: "bar",
		data: chartData,
		options: {
			scales: {
				y: {
					beginAtZero: true,
				},
			},
			plugins: {
				title: {
					display: true,
					text: "Monthly Sales Amount",
					position: "top",
					font: {
						size: 40,
						weight: "bold",
						family: "Montserrat, sans-serif",
					},
					color: "rgb(0, 0, 0)",
				},
			},
		},
	};
	new Chart(chart, config);
}
