public class CourseScheduler {
    public static void CustomSort(int[][] courses) {
        for (int i = 0; i <= courses.length - 1; i++) {
            for (int j = i + 1; j < courses.length; j++) {
                if (courses[i][0] > courses[j][0]) {
                    int[] temp = courses[i];
                    courses[i] = courses[j];
                    courses[j] = temp;
                }
            }
        }
    }

    public static int maxNonOverlappingCourses(int[][] courses) {
        CustomSort(courses);
        if (courses.length == 0) {
            return 0;
        }
        int end = courses[0][1];
        int count = 1;
        for(int[] course : courses){
            if (course[0] >= end){
                count++;
                end = course[1];
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(maxNonOverlappingCourses(new int[][]{{9, 11}, {10, 12}, {11, 13}, {15, 16}}));
        System.out.println(maxNonOverlappingCourses(new int[][]{{19, 22}, {17, 19}, {9, 12}, {9, 11}, {15, 17}, {15, 17}}));
        System.out.println(maxNonOverlappingCourses(new int[][]{{13, 15}, {13, 17}, {11, 17}}));
        System.out.println(maxNonOverlappingCourses(new int[][]{{19, 22}}));
    }
}