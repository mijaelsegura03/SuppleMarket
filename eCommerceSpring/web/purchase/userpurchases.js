import { renderUserDropdown } from "../utils/dropdowns/userdropdown.js";
import { extractDni } from "../utils/jwt/extractDni.js";
const PURCHASES_URL = "http://localhost:8080/purchases/dni";
const SUPPLEMENTS_URL = "http://localhost:8080/supplements";
const purchasesAccordion = document.getElementById("purchases-accordion");

document.addEventListener("DOMContentLoaded", () => {
  renderUserDropdown();
  fillAccordionWithDataAndListeners();
});

async function fillAccordionWithDataAndListeners() {
  await renderPurchases();
  addAccordionEventListeners();
}

async function renderPurchases() {
  const jwt = sessionStorage.getItem("access_token");
  const dni = extractDni(jwt);
  const response = await fetch(`${PURCHASES_URL}/${dni}`, {
    headers: {
      Authorization: `Bearer ${jwt}`,
    },
  });
  const data = await response.json();
  const purchases = data.purchases;
  let idx = 1
  purchases.forEach((purchase) => {
    const purchaseDetail = purchase.purchaseDetails;
    const date = purchase.purchaseDate;
    const accordionItem = document.createElement("div");
    accordionItem.classList.add("accordion-item");
    const accordionHeader = document.createElement("div");
    accordionHeader.classList.add("accordion-header");
    accordionHeader.innerHTML = `<p>${date}</p><i class="fa-solid fa-plus"></i>`;
    const accordionContent = document.createElement("div");
    accordionContent.classList.add("accordion-content");
    accordionContent.innerHTML = `<p class="purchase-total">Total: $${purchase.totalPrice}</p>
                                      <div class="purchase-details">
                                          <p>Products:</p>
                                          <ul class="list-item" id="purchase-details-list-${idx}">
                                          </ul>
                                      </div>`;

    accordionItem.appendChild(accordionHeader);
    accordionItem.appendChild(accordionContent);
    purchasesAccordion.appendChild(accordionItem);
    renderDetails(purchaseDetail, jwt, idx);
    idx++;
  });
}

async function renderDetails(purchaseDetail, jwt, idx) {
  const detailsList = document.getElementById(`purchase-details-list-${idx}`);
    for (const detail of purchaseDetail) {
        const response = await fetch(`${SUPPLEMENTS_URL}/${detail.supplementId}`, {
            headers: {
                Authorization: `Bearer ${jwt}`
            }
        });
        const data = await response.json();
        const supplement = data.supplement;
        const supplementName = supplement.name;
        const supplementTotalPrice = supplement.unitaryPrice * detail.quantity;
        const listItem = document.createElement("li");
        listItem.textContent = `${detail.quantity} x ${supplementName} - $${supplementTotalPrice}`;
        detailsList.appendChild(listItem);
    }
}

function addAccordionEventListeners() {
  const accordionHeaders = document.querySelectorAll(".accordion-header");
  const accordionContents = document.querySelectorAll(".accordion-content");
  accordionHeaders.forEach((header) => {
    header.addEventListener("click", () => {
      const accordionItem = header.parentElement;
      const accordionContent = accordionItem.querySelector(".accordion-content");
      header.classList.toggle("active");

      accordionContents.forEach((content) => {
        if (content !== accordionContent) {
          content.parentElement.querySelector(".accordion-header").classList.remove("active");
          content.classList.remove("active");
          content.style.maxHeight = 0;
          content.style.padding = 0
        }
      });

      accordionContent.classList.toggle("active");

      if (accordionContent.classList.contains("active")) {
        let addedHeight = accordionContent.querySelector(".list-item").children.length * 10 + 20
        accordionContent.style.maxHeight = accordionContent.scrollHeight + addedHeight + "px";
        accordionContent.style.padding = 10 + "px"
      } else {
        accordionContent.style.maxHeight = 0;
        accordionContent.style.padding = 0;
      }
    });
  });
}
