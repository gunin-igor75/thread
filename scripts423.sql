SELECT s.name, s.age, f.name
FROM student s
         JOIN faculty f on s.faculty_id = f.id;

SELECT s.name, s.age FROM student s
                  JOIN avatar a on s.id = a.student_id;