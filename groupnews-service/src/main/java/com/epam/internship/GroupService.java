package com.epam.internship;

import com.epam.internship.dto.Group;

public interface GroupService {

	Group ceateGoup(Long userId, String title, String description);
}
