// Notification
const sectionIcons = {
    workout: "🏋️‍♂️",
    activity: "⚡",
    nutrition: "🥗",
    chat: "💬"
};

let notificationNav=getId("noti-btn");
const notifications = {
    workout: [
        {
            title: "New Workout Plan Available",
            message: "Your personalized HIIT workout is ready. 30 minutes of intense cardio and strength training."
        },
        {
            title: "Workout Achievement",
            message: "Congratulations! You've completed 5 workouts this week. Keep up the great work! 💪"
        },
        {
            title: "Recovery Day Reminder",
            message: "Today is your scheduled recovery day. Focus on stretching and light activities."
        }
    ],
    activity: [
        {
            title: "Daily Goal Reached",
            message: "You've hit your 10,000 steps goal! Amazing job staying active today! 🎉"
        },
        {
            title: "New Activity Challenge",
            message: "Join the weekend warrior challenge: Complete 3 different activities this weekend."
        },
        {
            title: "Activity Streak",
            message: "You're on a 5-day activity streak! Keep the momentum going! ⚡"
        }
    ],
    nutrition: [
        {
            title: "Meal Plan Updated",
            message: "Your weekly meal plan has been updated with new healthy recipes. Check it out! 🥗"
        },
        {
            title: "Hydration Reminder",
            message: "Don't forget to stay hydrated! You're 2 glasses away from your daily water goal. 💧"
        },
        {
            title: "Nutrition Tip",
            message: "Try adding more leafy greens to your diet for increased energy and better recovery."
        }
    ],
    chat: [
        {
            title: "New Message from Coach",
            message: "Sarah: Great progress on your form! Let's schedule a session to work on your technique. 📱"
        },
        {
            title: "Community Challenge",
            message: "Your fitness group has started a new challenge. Join now to participate! 🤝"
        },
        {
            title: "Support Message",
            message: "Your workout buddy Tom sent you a motivation boost: 'Let's crush today's workout!' 💪"
        }
    ]
};
const SIDE_BAR=_(".sidebar");



function showAllNotifications() {
    const panel = document.getElementById('notificationPanel');
    const content = document.getElementById('notificationContent');
    
    // Clear previous content
    content.innerHTML = '';
    
    // Add notifications for each category
    Object.keys(notifications).forEach(type => {
        const sectionElement = document.createElement('div');
        sectionElement.className = `notification-section ${type}-section`;
        
        // Add section title
        sectionElement.innerHTML = `
            <div class="section-title">
                <span>${sectionIcons[type]}</span>
                ${type.charAt(0).toUpperCase() + type.slice(1)}
            </div>
        `;
        
        // Add notifications for this section
        notifications[type].forEach(notification => {
            const notificationElement = document.createElement('div');
            notificationElement.className = `notification-item ${type}-notification`;
            notificationElement.innerHTML = `
                <h3>${notification.title}</h3>
                <p>${notification.message}</p>
            `;
            sectionElement.appendChild(notificationElement);
        });
        
        content.appendChild(sectionElement);
    });
    
    // Show panel
    panel.classList.add('show');
}


function hideNotification() {
    const panel = document.getElementById('notificationPanel');
    panel.classList.remove('show');
    notificationNav.classList.remove("active");
}

// Close notification panel when clicking outside
SIDE_BAR.addEventListener('click', (event) => {
    const panel = document.getElementById('notificationPanel');
    const button = document.getElementById('.noti-btn');
    
    if (!panel.contains(event.target) && !button.contains(event.target)) {
        hideNotification();

    }
});

// setting

  document.addEventListener('DOMContentLoaded', () => {
      const settingsToggle = document.getElementById('settingsToggle');
      const settingsModal = document.getElementById('settingsModal');

      settingsToggle.addEventListener('click', () => {
        settingsModal.classList.add('visible');
        settingsToggle.classList.add("active");
      });

      settingsModal.addEventListener('click', (e) => {
        if (e.target === settingsModal) {
          settingsModal.classList.remove('visible');
           settingsToggle.classList.remove("active");
        }
      });

      document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && settingsModal.classList.contains('visible')) {
          settingsModal.classList.remove('visible');
          settingsToggle.classList.remove("active");
        }
      });

      const navButtons = document.querySelectorAll('.nav-button');
      const tabContents = document.querySelectorAll('.tab-content');
      const themeButtons = document.querySelectorAll('.theme-button');

      navButtons.forEach(button => {
        button.addEventListener('click', () => {
          navButtons.forEach(btn => btn.classList.remove('activesetting'));
          tabContents.forEach(content => content.classList.remove('activesetting'));
          
          button.classList.add('activesetting');
          const tabId = button.getAttribute('data-tab');
          document.getElementById(tabId).classList.add('activesetting');
        });
      });

      themeButtons.forEach(button => {
        button.addEventListener('click', () => {
          themeButtons.forEach(btn => btn.classList.remove('activesetting'));
          button.classList.add('activesetting');
        });
      });

      const toggles = document.querySelectorAll('.toggle input');
      toggles.forEach(toggle => {
        toggle.addEventListener('change', () => {
          const settingName = toggle.closest('.setting-item').querySelector('label').textContent;
          console.log(`${settingName} is ${toggle.checked ? 'enabled' : 'disabled'}`);
        });
      });

      const typeItems = document.querySelectorAll('.type-item');
      typeItems.forEach(item => {
        item.addEventListener('click', () => {
          const typeName = item.querySelector('.type-icon-label').textContent.trim();
          console.log(`Opening ${typeName} settings`);
        });
      });
    });

    // profile

            document.addEventListener('DOMContentLoaded', () => {
    const profileImage = document.getElementById('profileImage');
    const imageUpload = document.getElementById('imageUpload');
    const profileForm = document.getElementById('profileForm');
    const nameInput = document.getElementById('name');
    const aboutInput = document.getElementById('about');
    const modal = document.getElementById('profileModal');
    const userIconButton = document.querySelector('.user-icon-button');
    const closeButton = document.querySelector('.close-button');
    const modalOverlay = document.querySelector('.modal-overlay');

    // Toggle modal
    function toggleModal() {
        modal.classList.toggle('active');
        document.body.style.overflow = modal.classList.contains('active') ? 'hidden' : '';
        userIconButton.classList.toggle("active");
    }
	
	//dash
	
	getId("profile-tab").addEventListener("click",toggleModal)

    // Open modal when user icon is clicked
    userIconButton.addEventListener('click', toggleModal);

    // Close modal when close button or overlay is clicked
    closeButton.addEventListener('click', toggleModal);
    modalOverlay.addEventListener('click', toggleModal);

    // Close modal on escape key
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && modal.classList.contains('active')) {
            toggleModal();
        }
    });

    // Handle profile image click
    document.querySelector('.profile-image-wrapper').addEventListener('click', () => {
        imageUpload.click();
    });

    // Handle image upload
    imageUpload.addEventListener('change', (event) => {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                profileImage.src = e.target.result;
                
                // Add a subtle animation
                profileImage.style.transform = 'scale(1.05)';
                setTimeout(() => {
                    profileImage.style.transform = 'scale(1)';
                }, 200);
            };
            reader.readAsDataURL(file);
        }
    });

    // Handle form submission
    profileForm.addEventListener('submit', (e) => {
        e.preventDefault();
        
        // Get form data
        const formData = {
            name: nameInput.value,
            email: document.getElementById('email').value,
            about: aboutInput.value
        };

        // Simulate API call
        console.log('Updating profile:', formData);
        
        // Show success message with animation
        const button = e.target.querySelector('button');
        const originalText = button.textContent;
        button.textContent = 'Saving...';
        button.style.backgroundColor = '#2563eb';
        
        setTimeout(() => {
            button.textContent = 'Saved!';
            button.style.backgroundColor = '#059669';
            
            setTimeout(() => {
                button.textContent = originalText;
                button.style.backgroundColor = '#3b82f6';
                toggleModal(); // Close modal after successful save
            }, 1500);
        }, 1000);
    });

    // Add input animations
    const inputs = document.querySelectorAll('input:not([readonly]), textarea');
    inputs.forEach(input => {
        input.addEventListener('focus', () => {
            input.parentElement.style.transform = 'translateY(-2px)';
        });

        input.addEventListener('blur', () => {
            input.parentElement.style.transform = 'translateY(0)';
        });
    });
});

// logout
document.addEventListener('DOMContentLoaded', () => {
    const logoutBtn = document.getElementById('logoutBtn');
    const logoutDialog = document.getElementById('logoutDialog');
    const cancelBtn = document.getElementById('cancelBtn');
    const confirmBtn = document.getElementById('confirmBtn');

    // Open dialog
    logoutBtn.addEventListener('click', () => {
        logoutDialog.classList.add('activelogout');
        logoutBtn.classList.add('active')
    });

    // Close dialog on cancel
    cancelBtn.addEventListener('click', () => {
        logoutDialog.classList.remove('activelogout');
        logoutBtn.classList.remove('active')
    });

    // Close dialog on clicking outside
    logoutDialog.addEventListener('click', (e) => {
        if (e.target === logoutDialog) {
            logoutDialog.classList.remove('activelogout');
            logoutBtn.classList.remove('active')
        }
    });

    // Handle logout
    confirmBtn.addEventListener('click', () => {
        console.log('Logging out...');
        // Add your logout logic here
        logoutDialog.classList.remove('activelogout');
        logoutBtn.classList.remove('active')
        
        fetch("logout")
        .then((resp)=>{
			if(resp.ok){
				return resp;
			}
		}).then((data)=>{
			console.log(data);
			window.location.href ="kuntoLandingPage.html";
			//dispPopUp(data.message);
		});
    });

    // Close on escape key
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && logoutDialog.classList.contains('activelogout')) {
            logoutDialog.classList.remove('activelogout');
            logoutBtn.classList.remove('active')

        }
    });
});